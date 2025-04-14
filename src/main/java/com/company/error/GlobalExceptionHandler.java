package com.company.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException ex,  WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.valueOf(ex.getStatusCode().value()), ex.getMessage()
        );
        problemDetail.setTitle(ex.getReason());

        return buildResponseEntity(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), ex.getMessage(), request );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> details = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Validation Failed", "Invalid request parameters", request, details);
    }

    private ResponseEntity<ProblemDetail> buildResponseEntity(HttpStatus status, String error, String message, WebRequest request) {
        return buildResponseEntity(status, error, message, request, null);
    }

    private ResponseEntity<ProblemDetail> buildResponseEntity(HttpStatus status, String error, String message, WebRequest request, List<String> details) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(error);
        problemDetail.setInstance(URI.create(request.getDescription(false)));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("details", details);
        return ResponseEntity.status(status).body(problemDetail);
    }
}