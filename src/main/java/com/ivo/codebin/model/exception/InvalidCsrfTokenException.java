package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.ForbiddenException;

public class InvalidCsrfTokenException extends ForbiddenException {
    public InvalidCsrfTokenException(String message) {
        super(message);
    }
}
