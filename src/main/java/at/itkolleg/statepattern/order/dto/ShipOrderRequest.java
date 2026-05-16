package at.itkolleg.statepattern.order.dto;

import jakarta.validation.constraints.NotBlank;

public record ShipOrderRequest(
        @NotBlank(message = "trackingNumber darf nicht leer sein")
        String trackingNumber
) {
}