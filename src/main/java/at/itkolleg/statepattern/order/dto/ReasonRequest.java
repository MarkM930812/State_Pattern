package at.itkolleg.statepattern.order.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Gemeinsames Request-DTO für Storno- und Retourgründe.
 */
public record ReasonRequest(
        @NotBlank(message = "reason darf nicht leer sein")
        String reason
) {
}