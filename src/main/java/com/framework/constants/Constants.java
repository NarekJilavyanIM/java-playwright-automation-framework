package com.framework.constants;

import com.framework.config.ConfigManager;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Constants {

    private Constants() {
    }

    public static final class Timeouts {
        public static final int DEFAULT_MS = 30_000;
        public static final int CLICK_MS = 5_000;
        public static final int WAIT_VISIBLE_MS = 15_000;
        public static final int URL_WAIT_MS = 8_000;
        public static final int SCROLL_MS = 10_000;
        public static final int CLICK_ATTEMPTS = 3;
        public static final int VIEWPORT_WIDTH = 1920;
        public static final int VIEWPORT_HEIGHT = 1080;
        public static final int SCROLL_TOP_PX = 250;

        private Timeouts() {
        }
    }

    public static final class TestData {
        public static final String BASE_URL =
                "https://www.automationexercise.com";
        public static final String HEADLESS_DEFAULT = "true";

        public static final String SEARCH_PRODUCT = "tshirt";
        public static final String[] SEARCH_PRODUCT_OPTIONS =
                {"tshirt", "t-shirt", "t shirt"};

        private TestData() {}
    }

    public static final class Routes {
        public static final String LOGIN = "/login";
        public static final String PRODUCTS = "/products";
        public static final String VIEW_CART = "/view_cart";
        public static final String DELETE_ACCOUNT = "/delete_account";

        private Routes() {
        }

        public static String absolute(String path) {
            return ConfigManager.getInstance().getBaseUrl() + path;
        }
    }

    public static final class Paths {
        public static final Path TRACES_DIRECTORY = Path.of("target", "traces");
        public static final String TRACE_FILE_PREFIX_ORPHAN = "orphan-";
        public static final String TRACE_FILE_EXTENSION = ".zip";
        public static final DateTimeFormatter TRACE_TIMESTAMP =
                DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        public static final String ALLURE_SCREENSHOT = "Screenshot";
        public static final String ALLURE_FAILURE_SCREENSHOT =
                "Failure screenshot";
        public static final String ALLURE_PAGE_URL = "Page URL";
        public static final String ALLURE_TRACE = "Playwright Trace";
        public static final String MIME_PNG = "image/png";
        public static final String MIME_PLAIN = "text/plain";
        public static final String MIME_ZIP = "application/zip";
        public static final String EXTENSION_PNG = "png";
        public static final String EXTENSION_ZIP = "zip";

        private Paths() {
        }
    }

    public enum GenderTitle {
        MR("Mr"),
        MRS("Mrs");

        private final String value;

        GenderTitle(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public boolean matches(String other) {
            return value.equalsIgnoreCase(other);
        }
    }

    public enum Country {
        UNITED_STATES("United States");

        private final String value;

        Country(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public enum BrowserName {
        CHROMIUM("chromium"),
        FIREFOX("firefox"),
        WEBKIT("webkit");

        private final String value;

        BrowserName(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static BrowserName from(String raw) {
            if (raw == null || raw.isBlank()) {
                return CHROMIUM;
            }
            return switch (raw.trim().toLowerCase(Locale.ROOT)) {
                case "firefox" -> FIREFOX;
                case "webkit", "safari" -> WEBKIT;
                default -> CHROMIUM;
            };
        }
    }
}
