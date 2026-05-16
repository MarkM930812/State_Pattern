package at.itkolleg.statepattern.order.state;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

@Component
public class PaidOrderState extends AbstractOrderState {

    @Override
    public OrderStatus supports() {
        return OrderStatus.PAID;
    }

    @Override
    public void pack(KitOrder order) {
        order.setStatus(OrderStatus.PACKED);
        order.setPackedAt(LocalDateTime.now());
    }

    @Override
    public void cancel(KitOrder order, String reason) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(requireText(reason, "reason"));
        order.setCancelledAt(LocalDateTime.now());
    }
}