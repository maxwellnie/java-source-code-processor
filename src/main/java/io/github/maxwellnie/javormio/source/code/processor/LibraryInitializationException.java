package io.github.maxwellnie.javormio.source.code.processor;

/**
 * @author Maxwell Nie
 */
public class LibraryInitializationException extends Exception{
    public LibraryInitializationException() {
    }

    public LibraryInitializationException(String message) {
        super(message);
    }

    public LibraryInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibraryInitializationException(Throwable cause) {
        super(cause);
    }

    public LibraryInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
