package at.itkolleg.statepattern.order.state;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * State für versendete Bestellungen.
 */
@Component
public class ShippedOrderState extends AbstractOrderState {

    @Override
    public OrderStatus supports() {
        return OrderStatus.SHIPPED;
    }

    @Override
    public void returnOrder(KitOrder order, String reason) {
        order.setStatus(OrderStatus.RETURNED);
        order.setReturnReason(requireText(reason, "reason"));
        order.setReturnedAt(LocalDateTime.now());
    }
}