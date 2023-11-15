package com.stackexcelero.dataAccess.exception;

public class NullUserException extends RuntimeException {
	public NullUserException() {
        super("User object cannot be null or totally blank.");
    }

    public NullUserException(String message) {
        super(message);
    }

    // If you want to provide an exception with a cause
    public NullUserException(String message, Throwable cause) {
        super(message, cause);
    }

    // If you want to provide an exception with a cause without a message
    public NullUserException(Throwable cause) {
        super(cause);
    }

    // Optional: if you want to enable suppression and writable stack traces
    public NullUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
