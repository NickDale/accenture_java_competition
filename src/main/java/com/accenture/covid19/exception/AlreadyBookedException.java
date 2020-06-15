package com.accenture.covid19.exception;

public class AlreadyBookedException extends RuntimeException {

    public AlreadyBookedException() {
        super();
    }

    /**
     * Constructs a new <code>AlreadyBookedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public AlreadyBookedException(String message) {
        super(message);
    }
}
