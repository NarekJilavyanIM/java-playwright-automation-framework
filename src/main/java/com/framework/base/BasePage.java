package com.framework.base;

import com.framework.constants.AdsConstants;
import com.framework.constants.Constants.Timeouts;
import com.framework.logging.CatchHandler;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

public abstract class BasePage {

    protected BasePage() {}

    protected Page page() {
        return PageContext.get();
    }

    public void navigateTo(String url) {
        CatchHandler.getInstance().abortIfRequested();
        try {
            page().navigate(url,
                new Page.NavigateOptions().setWaitUntil(
                    WaitUntilState.DOMCONTENTLOADED));
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndAbort("navigate to " + url,
                exception);
        }
    }

    protected void safeClick(Locator locator) {
        CatchHandler.getInstance().abortIfRequested();
        RuntimeException lastError = null;
        for (int attempt = 1; attempt <= Timeouts.CLICK_ATTEMPTS; attempt++) {
            try {
                locator.click(new Locator.ClickOptions().setTimeout(
                        Timeouts.CLICK_MS));
                return;
            } catch (RuntimeException exception) {
                lastError = exception;
            }
        }
        CatchHandler.getInstance().logAndAbort("click " + locator, lastError);
    }

    protected void safeClickAndExpectUrl(Locator locator, String urlFragment,
            String fallbackUrl) {
        CatchHandler.getInstance().abortIfRequested();
        for (int attempt = 1; attempt <= Timeouts.CLICK_ATTEMPTS; attempt++) {
            try {
                locator.click(new Locator.ClickOptions().setTimeout(
                        Timeouts.CLICK_MS));
                page().waitForURL("**" + urlFragment + "*",
                        new Page.WaitForURLOptions().setTimeout(
                                Timeouts.URL_WAIT_MS));
                if (page().url().contains(urlFragment)
                        && !page().url().contains(
                                AdsConstants.GOOGLE_VIGNETTE_HASH)) {
                    return;
                }
            } catch (RuntimeException ignored) {
                if(fallbackUrl != null) {
                    navigateTo(fallbackUrl);
                } else {
                    CatchHandler.getInstance().logAndAbort(
                            "click " + locator, ignored);
                }
            }
        }
    }

    protected void safeFill(Locator locator, String value) {
        CatchHandler.getInstance().abortIfRequested();
        try {
            locator.fill(value);
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndAbort("fill " + locator,
                    exception);
        }
    }

    protected void safeSelect(Locator locator, String value) {
        CatchHandler.getInstance().abortIfRequested();
        try {
            locator.selectOption(value);
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndAbort("select " + locator,
                    exception);
        }
    }

    protected void waitForVisible(Locator locator) {
        CatchHandler.getInstance().abortIfRequested();
        try {
            locator.waitFor(new Locator.WaitForOptions().setTimeout(
                    Timeouts.WAIT_VISIBLE_MS));
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndAbort(
                    "wait for visible " + locator, exception);
        }
    }

    public String currentUrl() {
        CatchHandler.getInstance().abortIfRequested();
        return page().url();
    }
}
