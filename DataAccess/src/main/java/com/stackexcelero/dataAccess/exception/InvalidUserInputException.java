package com.stackexcelero.dataAccess.exception;

public class InvalidUserInputException extends RuntimeException {
	public InvalidUserInputException() {
        super("User object cannot be null or totally blank.");
    }

    public InvalidUserInputException(String message) {
        super(message);
    }

    // If you want to provide an exception with a cause
    public InvalidUserInputException(String message, Throwable cause) {
        super(message, cause);
    }

    // If you want to provide an exception with a cause without a message
    public InvalidUserInputException(Throwable cause) {
        super(cause);
    }

    // Optional: if you want to enable suppression and writable stack traces
    public InvalidUserInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
