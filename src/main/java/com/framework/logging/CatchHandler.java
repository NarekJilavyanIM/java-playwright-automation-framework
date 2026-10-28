package com.framework.logging;

import org.opentest4j.TestAbortedException;

public final class CatchHandler {

    private static final CatchHandler INSTANCE = new CatchHandler();
    private static final ThreadLocal<TestAbortedException> PENDING_ABORT =
            new ThreadLocal<>();

    private CatchHandler() {
    }

    public static CatchHandler getInstance() {
        return INSTANCE;
    }

    public void logAndAbort(String operation, Throwable error) {
        String message =
                "Test skipped because '" + operation + "' failed: "
                        + errorMessage(error);
        TestAbortedException aborted = new TestAbortedException(message, error);
        PENDING_ABORT.set(aborted);
        throw aborted;
    }

    public void logAndRequestAbort(String operation, Throwable error) {
        String message =
                "Test skipped because '" + operation + "' failed: "
                        + errorMessage(error);
        PENDING_ABORT.set(new TestAbortedException(message, error));
    }

    public void abortIfRequested() {
        TestAbortedException pending = PENDING_ABORT.get();
        if (pending != null) {
            PENDING_ABORT.remove();
            throw pending;
        }
    }

    public void clear() {
        PENDING_ABORT.remove();
    }

    private static String errorMessage(Throwable error) {
        if (error == null) {
            return "unknown error";
        }
        String message = error.getMessage();
        return message == null
                || message.isBlank() ? error.getClass()
                        .getSimpleName() : message;
    }
}
