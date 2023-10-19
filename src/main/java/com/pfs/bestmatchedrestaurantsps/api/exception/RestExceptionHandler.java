package com.pfs.bestmatchedrestaurantsps.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pfs.bestmatchedrestaurantsps.api.response.RestErrorResponse;
import com.pfs.bestmatchedrestaurantsps.application.exception.ValidatorException;

import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestErrorResponse> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new RestErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(ValidatorException.class)
  public ResponseEntity<RestErrorResponse> handleException(ValidatorException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new RestErrorResponse(e.getMessage()));
  }

}