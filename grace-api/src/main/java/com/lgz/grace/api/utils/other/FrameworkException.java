package com.lgz.grace.api.utils.other;

public class FrameworkException extends Exception {
    private static final long serialVersionUID = 405082117695663275L;

    public FrameworkException() {
    }

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(Throwable cause) {
        super(cause);
    }
}
