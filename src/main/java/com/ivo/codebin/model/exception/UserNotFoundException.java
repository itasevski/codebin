package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
