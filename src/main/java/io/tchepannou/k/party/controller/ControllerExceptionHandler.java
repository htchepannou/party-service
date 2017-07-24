package io.tchepannou.k.party.controller;

import io.tchepannou.k.party.ErrorResponse;
import io.tchepannou.k.party.exception.BusinessException;
import io.tchepannou.k.party.exception.Error;
import io.tchepannou.k.party.exception.InvalidRequestException;
import io.tchepannou.k.party.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException ex) {
        final ErrorResponse response = createErrorResponse(ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final InvalidRequestException ex) {
        final ErrorResponse response = createErrorResponse(ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException ex) {
        final ErrorResponse response = createErrorResponse(ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }


    private ErrorResponse createErrorResponse(final BusinessException ex){
        final ErrorResponse response = new ErrorResponse();
        response.getErrors().add(createError(ex.getErrorCode()));
        return response;
    }

    private io.tchepannou.k.party.Error createError(Error code){
        final io.tchepannou.k.party.Error error = new io.tchepannou.k.party.Error();
        error.setCode(code.getCode());
        error.setDescription(code.getDescription());
        return error;
    }
}
