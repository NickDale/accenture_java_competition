package com.accenture.covid19.exception;

public class InvalidDateException extends RuntimeException {

    public InvalidDateException() {
        super();
    }

    /**
     * Constructs a new <code>InvalidDateException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidDateException(String message) {
        super(message);
    }
}

