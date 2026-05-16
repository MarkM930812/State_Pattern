package at.itkolleg.statepattern.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * Response-DTO der REST-API.
 */
public record KitOrderResponse(
        Long id,
        String customerName,
        String customerEmail,
        String shippingAddress,
        String kitName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        OrderStatus status,
        String trackingNumber,
        String cancellationReason,
        String returnReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime paidAt,
        LocalDateTime packedAt,
        LocalDateTime shippedAt,
        LocalDateTime cancelledAt,
        LocalDateTime returnedAt
) {

    public static KitOrderResponse from(KitOrder order) {
        // Einheitliches Mapping hält die REST-Antwort stabil, auch wenn sich die Entity intern weiterentwickelt.
        return new KitOrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getShippingAddress(),
                order.getKitName(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getTrackingNumber(),
                order.getCancellationReason(),
                order.getReturnReason(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getPaidAt(),
                order.getPackedAt(),
                order.getShippedAt(),
                order.getCancelledAt(),
                order.getReturnedAt()
        );
    }
}