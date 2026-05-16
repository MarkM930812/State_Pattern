package at.itkolleg.statepattern.order.state;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * Endzustand für stornierte Bestellungen.
 */
@Component
public class CancelledOrderState extends AbstractOrderState {

    @Override
    public OrderStatus supports() {
        return OrderStatus.CANCELLED;
    }
}