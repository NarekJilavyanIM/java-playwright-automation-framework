package com.framework.ads;

import com.framework.constants.AdsConstants;
import com.framework.logging.CatchHandler;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;

import java.util.Locale;

/**
 * Neutralizes Automation Exercise ads: Google Vignette hash injection,
 */
public final class AdHandler {

    private static final AdHandler INSTANCE = new AdHandler();

    private AdHandler() {
    }

    public static AdHandler getInstance() {
        return INSTANCE;
    }

    public void attach(Page page) {
        page.context().route("**/*", this::filterAdRequests);
        page.onLoad(loadedPage -> sanitizeVignetteUrl(loadedPage));
        page.onFrameNavigated(frame -> {
            if (frame == page.mainFrame()) {
                sanitizeVignetteUrl(page);
            }
        });
        page.onPopup(popup -> {
            try {
                popup.close();
            } catch (RuntimeException exception) {
                CatchHandler.getInstance().logAndRequestAbort("close ad popup",
                        exception);
            }
        });
    }

    public void sanitizeVignetteUrl(Page page) {
        try {
            if (page.url().contains(AdsConstants.GOOGLE_VIGNETTE_HASH)) {
                page.evaluate(
                        "history.replaceState('', document.title, "
                                + "window.location.pathname + "
                                + "window.location.search);"
                );
            }
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndRequestAbort(
                    "sanitize #google_vignette URL", exception);
        }
    }

    private void filterAdRequests(Route route) {
        String url = route.request().url().toLowerCase(Locale.ROOT);
        boolean isAd = AdsConstants.AD_URL_FRAGMENTS.stream().anyMatch(
                url::contains);
        if (isAd) {
            route.abort();
        } else {
            route.resume();
        }
    }
}
