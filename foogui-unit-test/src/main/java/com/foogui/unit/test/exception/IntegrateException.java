package com.foogui.unit.test.exception;

public class IntegrateException extends  RuntimeException {

    public IntegrateException(String message, Object... params) {
        super(String.format(message, params));
    }
}