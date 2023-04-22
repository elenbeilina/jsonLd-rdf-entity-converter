package com.aqualen.springjsonldrdfdtoconverter.exception;

public class ContextException extends RuntimeException {

    public ContextException(String exception) {
        super(exception);
    }

    public ContextException(String exception, Throwable cause) {
        super(exception, cause);
    }
}
