package com.ardas.stockorderservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MESSAGE_TEMPLATE = "{} - {}";
    private static final String DEFAULT_CODE = "STOS001";

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleRecordNotFoundException(BaseException e) {
        log.error(MESSAGE_TEMPLATE, e.getCode(), e.getMessage(), e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCode(), e.getMessage(), e.getDetails()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(DEFAULT_CODE, exception.getMessage(), Collections.emptyMap());
        log.error(MESSAGE_TEMPLATE, errorResponse.code(), errorResponse.message(), exception);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

}
