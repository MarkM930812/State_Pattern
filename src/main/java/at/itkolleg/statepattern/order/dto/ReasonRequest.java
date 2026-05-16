package at.itkolleg.statepattern.order.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Gemeinsames Request-DTO für Aktionen, die einen fachlichen Grund benötigen.
 */
public record ReasonRequest(
        @NotBlank(message = "reason darf nicht leer sein")
        String reason
) {
}