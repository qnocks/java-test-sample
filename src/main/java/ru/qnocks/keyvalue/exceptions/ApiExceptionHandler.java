package ru.qnocks.keyvalue.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(BadRequestException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(), httpStatus, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Object> handleApiRequestException(InternalServerErrorException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(), httpStatus, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
