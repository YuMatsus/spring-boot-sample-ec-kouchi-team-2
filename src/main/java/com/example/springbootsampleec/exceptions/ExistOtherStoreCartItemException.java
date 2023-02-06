package com.example.springbootsampleec.exceptions;

public class ExistOtherStoreCartItemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExistOtherStoreCartItemException() {
        super();
    }

    public ExistOtherStoreCartItemException(String message) {
        super(message);
    }

    public ExistOtherStoreCartItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistOtherStoreCartItemException(Throwable cause) {
        super(cause);
    }

    protected ExistOtherStoreCartItemException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
