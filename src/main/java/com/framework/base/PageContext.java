package com.framework.base;

import com.microsoft.playwright.Page;

public final class PageContext {

    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private PageContext() {
    }

    public static void set(Page page) {
        PAGE.set(page);
    }

    public static Page get() {
        Page page = PAGE.get();
        if (page == null) {
            throw new IllegalStateException(
                    "Playwright page is not initialized for this thread");
        }
        return page;
    }

    public static Page getOrNull() {
        return PAGE.get();
    }

    public static void clear() {
        PAGE.remove();
    }
}
