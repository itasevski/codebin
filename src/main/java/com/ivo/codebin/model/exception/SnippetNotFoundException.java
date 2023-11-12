package com.ivo.codebin.model.exception;

import com.ivo.codebin.model.exception.base.ResourceNotFoundException;

public class SnippetNotFoundException extends ResourceNotFoundException {
    public SnippetNotFoundException(String message) {
        super(message);
    }
}
