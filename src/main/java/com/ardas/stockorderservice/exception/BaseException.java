package com.ardas.stockorderservice.exception;

import java.util.Map;

public abstract class BaseException extends RuntimeException {

    protected BaseException() {
        super();
    }

    public abstract Map<String, Object> getDetails();

    public abstract String getCode();

}
