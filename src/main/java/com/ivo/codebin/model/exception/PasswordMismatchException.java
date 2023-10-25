package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.BadRequestException;

public class PasswordMismatchException extends BadRequestException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}
