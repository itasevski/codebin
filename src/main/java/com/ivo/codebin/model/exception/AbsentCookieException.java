package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.ForbiddenException;

public class AbsentCookieException extends ForbiddenException {
    public AbsentCookieException(String message) {
        super(message);
    }
}
