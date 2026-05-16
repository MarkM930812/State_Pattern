package at.itkolleg.statepattern.order.state;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

@Component
public class PackedOrderState extends AbstractOrderState {

    @Override
    public OrderStatus supports() {
        return OrderStatus.PACKED;
    }

    @Override
    public void ship(KitOrder order, String trackingNumber) {
        order.setStatus(OrderStatus.SHIPPED);
        order.setTrackingNumber(requireText(trackingNumber, "trackingNumber"));
        order.setShippedAt(LocalDateTime.now());
    }

    @Override
    public void cancel(KitOrder order, String reason) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(requireText(reason, "reason"));
        order.setCancelledAt(LocalDateTime.now());
    }
}