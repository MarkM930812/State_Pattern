package at.itkolleg.statepattern.order.state;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * Endzustand für retournierte Bestellungen.
 */
@Component
public class ReturnedOrderState extends AbstractOrderState {

    @Override
    public OrderStatus supports() {
        return OrderStatus.RETURNED;
    }
}