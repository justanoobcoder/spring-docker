package com.hiepnh.springdocker.exception;

public class NotFoundException extends ApiException {
    public NotFoundException(String errorCode, Object... var2) {
        super(errorCode, var2);
    }
}
