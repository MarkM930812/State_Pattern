package at.itkolleg.statepattern.order.state;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

@Component
public class CreatedOrderState extends AbstractOrderState {

    @Override
    public OrderStatus supports() {
        return OrderStatus.CREATED;
    }

    @Override
    public void pay(KitOrder order) {
        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
    }

    @Override
    public void cancel(KitOrder order, String reason) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(requireText(reason, "reason"));
        order.setCancelledAt(LocalDateTime.now());
    }
}