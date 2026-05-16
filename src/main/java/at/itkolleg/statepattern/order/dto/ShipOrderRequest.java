package at.itkolleg.statepattern.order.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request-DTO für den Versand einer Bestellung.
 */
public record ShipOrderRequest(
        @NotBlank(message = "trackingNumber darf nicht leer sein")
        String trackingNumber
) {
}