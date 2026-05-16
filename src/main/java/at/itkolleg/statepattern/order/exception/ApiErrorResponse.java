package at.itkolleg.statepattern.order.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Einheitliches JSON-Format für Fehlermeldungen der REST-API.
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> details,
        String path
) {
}