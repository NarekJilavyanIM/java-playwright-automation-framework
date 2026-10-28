package com.framework.listeners;

import com.framework.base.BaseTest;
import com.framework.constants.Constants.Paths;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * After every test, while the page is still open, attaches a screenshot,
 * the current URL, and a Playwright trace to Allure.
 * JUnit runs {@link AfterTestExecutionCallback} before {@code @AfterEach}.
 */
public class TestWatcherExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        boolean failed = context.getExecutionException().isPresent();
        attachScreenshot(failed);
        attachPageUrl();
        attachTrace(context);
    }

    private void attachScreenshot(boolean failed) {
        Page page = BaseTest.getPage();
        if (page == null) {
            return;
        }
        try {
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true));
            String name =
                    failed ? Paths.ALLURE_FAILURE_SCREENSHOT : Paths
                            .ALLURE_SCREENSHOT;
            Allure.addAttachment(name, Paths.MIME_PNG,
                    new ByteArrayInputStream(screenshot),
                    Paths.EXTENSION_PNG);
        } catch (RuntimeException ignored) {
            // A missing screenshot must not change the test result.
        }
    }

    private void attachPageUrl() {
        Page page = BaseTest.getPage();
        if (page == null) {
            return;
        }
        try {
            Allure.addAttachment(Paths.ALLURE_PAGE_URL, Paths.MIME_PLAIN,
                    page.url());
        } catch (RuntimeException ignored) {
            // A missing URL attachment must not change the test result.
        }
    }

    private void attachTrace(ExtensionContext context) {
        BrowserContext browserContext = BaseTest.getContext();
        if (browserContext == null || !BaseTest.isTracingStarted()) {
            return;
        }
        Path tracePath = traceFile(context);
        try {
            Files.createDirectories(Paths.TRACES_DIRECTORY);
            browserContext.tracing().stop(new Tracing.StopOptions().setPath(
                    tracePath));
            BaseTest.markTracingStopped();
            try (InputStream input = Files.newInputStream(tracePath)) {
                Allure.addAttachment(Paths.ALLURE_TRACE, Paths.MIME_ZIP, input,
                        Paths.EXTENSION_ZIP);
            }
        } catch (RuntimeException | IOException ignored) {
            BaseTest.markTracingStopped();
        }
    }

    private Path traceFile(ExtensionContext context) {
        String testName = sanitize(context.getDisplayName());
        String stamp = LocalDateTime.now().format(Paths.TRACE_TIMESTAMP);
        return Paths.TRACES_DIRECTORY.resolve(
                testName + "-" + stamp + Paths.TRACE_FILE_EXTENSION);
    }

    private String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
