package at.itkolleg.statepattern.order.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Globale Fehlerbehandlung für die REST-API.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(OrderNotFoundException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), List.of(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidOrderStateTransitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTransition(InvalidOrderStateTransitionException exception,
                                                                    HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), List.of(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception,
                                                             HttpServletRequest request) {
        List<String> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, "Validierungsfehler in der Anfrage.", details, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException exception,
                                                                  HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), List.of(), request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                                                           String message,
                                                           List<String> details,
                                                           String path) {
        // Zentrale Fabrikmethode für konsistente Fehlerantworten.
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                details,
                path
        ));
    }
}