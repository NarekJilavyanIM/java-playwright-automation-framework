package com.framework.base;

import com.framework.ads.AdHandler;
import com.framework.config.ConfigManager;
import com.framework.constants.AdsConstants;
import com.framework.constants.Constants.BrowserName;
import com.framework.constants.Constants.Paths;
import com.framework.constants.Constants.Timeouts;
import com.framework.listeners.TestWatcherExtension;
import com.framework.logging.CatchHandler;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(TestWatcherExtension.class)
public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;

    private static final ThreadLocal<BrowserContext> THREAD_LOCAL_CONTEXT =
            new ThreadLocal<>();
    private static final ThreadLocal<Boolean> TRACING_STARTED =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        playwright = Playwright.create();
        browser = launchBrowser();
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(Timeouts.VIEWPORT_WIDTH,
                        Timeouts.VIEWPORT_HEIGHT));
        startTracing(testInfo);

        Page page = context.newPage();
        page.setDefaultTimeout(ConfigManager.getInstance().getTimeout());

        // URL change listener for #google_vignette plus route blocking and
        // overlay cleanup.
        page.onLoad(loadedPage -> {
            try {
                String currentUrl = page.url();
                if (currentUrl.contains(AdsConstants.GOOGLE_VIGNETTE_HASH)) {
                    page.evaluate(
                            "history.pushState('', document.title, "
                                    + "window.location.pathname + "
                                    + "window.location.search);"
                    );
                }
            } catch (RuntimeException exception) {
                CatchHandler.getInstance().logAndRequestAbort(
                        "strip #google_vignette on load", exception);
            }
        });
        AdHandler.getInstance().attach(page);

        PageContext.set(page);
        THREAD_LOCAL_CONTEXT.set(context);
    }

    public static Page getPage() {
        return PageContext.getOrNull();
    }

    public static BrowserContext getContext() {
        return THREAD_LOCAL_CONTEXT.get();
    }

    public static boolean isTracingStarted() {
        return Boolean.TRUE.equals(TRACING_STARTED.get());
    }

    public static void markTracingStopped() {
        TRACING_STARTED.set(Boolean.FALSE);
    }

    @AfterEach
    public void tearDown() {
        try {
            stopTracingIfStillRunning();
            if (context != null) {
                context.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        } catch (RuntimeException ignored) {
            // Closing the browser must not change the test result.
        } finally {
            PageContext.clear();
            THREAD_LOCAL_CONTEXT.remove();
            TRACING_STARTED.remove();
            CatchHandler.getInstance().clear();
        }
    }

    private void startTracing(TestInfo testInfo) {
        String title = testInfo.getDisplayName();
        context.tracing().start(new Tracing.StartOptions()
                .setTitle(title)
                .setName(title)
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        TRACING_STARTED.set(Boolean.TRUE);
    }

    private void stopTracingIfStillRunning() {
        if (!isTracingStarted() || context == null) {
            return;
        }
        try {
            Path fallback = Paths.TRACES_DIRECTORY.resolve(
                    Paths.TRACE_FILE_PREFIX_ORPHAN + System.currentTimeMillis()
                            + Paths.TRACE_FILE_EXTENSION);
            Files.createDirectories(fallback.getParent());
            context.tracing().stop(new Tracing.StopOptions().setPath(fallback));
        } catch (Exception ignored) {
            // A leftover trace must not change the test result.
        } finally {
            markTracingStopped();
        }
    }

    private Browser launchBrowser() {
        BrowserType.LaunchOptions launchOptions =
                new BrowserType.LaunchOptions()
                .setHeadless(ConfigManager.getInstance().isHeadless());
        return switch (ConfigManager.getInstance().getBrowser()) {
            case FIREFOX -> playwright.firefox().launch(launchOptions);
            case WEBKIT -> playwright.webkit().launch(launchOptions);
            case CHROMIUM -> playwright.chromium().launch(launchOptions);
        };
    }
}
