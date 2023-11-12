package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.BadRequestException;

public class BadDtoException extends BadRequestException {
    public BadDtoException(String message) {
        super(message);
    }
}
