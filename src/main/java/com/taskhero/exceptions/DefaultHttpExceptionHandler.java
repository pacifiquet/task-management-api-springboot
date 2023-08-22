package com.taskhero.exceptions;

import com.taskhero.utils.Time;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultHttpExceptionHandler {

  @ExceptionHandler({
    NoSuchElementException.class,
    DataIntegrityViolationException.class,
    IllegalArgumentException.class,
    IllegalStateException.class,
    TemplateInputException.class,
    NullPointerException.class,
    RuntimeException.class
  })
  public ResponseEntity<ApiError> defaultHandler(Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ApiError.builder()
                .path(request.getServletPath())
                .message(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .localDateTime(Time.currentDateTime().toString())
                .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> defaultHandler(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ApiError.builder()
                .path(request.getServletPath())
                .message(
                    Objects.requireNonNull(e.getBindingResult().getFieldError())
                        .getDefaultMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .localDateTime(Time.currentDateTime().toString())
                .build());
  }
}
