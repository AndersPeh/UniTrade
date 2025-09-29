package com.doubleA.UniTrade.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Listens to exceptions from controllers
@ControllerAdvice
public class GlobalExceptionHandler {

  // If GlobalExceptionHandler receives EntityExistsException from controllers
  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<String> handlerAlreadyExists(EntityExistsException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handlerAlreadyExists(EntityNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handlerAlreadyExists(Exception ex) {
    return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
