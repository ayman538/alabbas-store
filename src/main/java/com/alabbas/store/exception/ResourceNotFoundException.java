package com.alabbas.store.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final Object[] args;

    public ResourceNotFoundException(String message) {
        super(message);
        this.args = new Object[0];
    }

    public ResourceNotFoundException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
