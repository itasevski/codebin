package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.BadRequestException;

public class UserExistsException extends BadRequestException {
    public UserExistsException(String message) {
        super(message);
    }
}
