package com.ivo.codebin.model.exception.advice;

import com.ivo.codebin.model.exception.base.BadRequestException;
import com.ivo.codebin.model.exception.base.ForbiddenException;
import com.ivo.codebin.model.exception.base.ResourceNotFoundException;
import com.ivo.codebin.model.response.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CodebinControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundException exception, WebRequest request) {
        return responseEntityException(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException exception, WebRequest request) {
        return responseEntityException(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        return responseEntityException(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException exception, WebRequest request) {
        return responseEntityException(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ExceptionResponse> responseEntityException(String message, HttpStatus httpStatus) {
        ExceptionResponse response = ExceptionResponse.builder().message(message).timestamp(new Date().toString()).build();
        return new ResponseEntity<>(response, httpStatus);
    }

}
