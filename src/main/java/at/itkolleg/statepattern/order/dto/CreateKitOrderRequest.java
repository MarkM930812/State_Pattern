package at.itkolleg.statepattern.order.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateKitOrderRequest(
        @NotBlank(message = "customerName darf nicht leer sein")
        String customerName,
        @NotBlank(message = "customerEmail darf nicht leer sein")
        @Email(message = "customerEmail muss eine gültige E-Mail-Adresse sein")
        String customerEmail,
        @NotBlank(message = "shippingAddress darf nicht leer sein")
        String shippingAddress,
        @NotBlank(message = "kitName darf nicht leer sein")
        String kitName,
        @Positive(message = "quantity muss größer als 0 sein")
        int quantity,
        @Positive(message = "unitPrice muss größer als 0 sein")
        BigDecimal unitPrice
) {
}