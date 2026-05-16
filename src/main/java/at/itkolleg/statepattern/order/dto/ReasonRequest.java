package at.itkolleg.statepattern.order.dto;

import jakarta.validation.constraints.NotBlank;

public record ReasonRequest(
        @NotBlank(message = "reason darf nicht leer sein")
        String reason
) {
}