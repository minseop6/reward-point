package com.test.rewardpoint.common.exception;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ExceptionResponse> handleServerException(ServerException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.from(exception);
        HttpStatus httpStatus = Arrays.stream(exception.getClass().getAnnotations()).findFirst()
                .filter(annotation -> annotation instanceof ResponseStatus)
                .map(responseStatus -> ((ResponseStatus) responseStatus).value())
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleServerException(RuntimeException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.createUnknownExceptionResponse();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
