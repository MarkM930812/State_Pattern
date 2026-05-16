package at.itkolleg.statepattern.order.state;

import at.itkolleg.statepattern.order.exception.InvalidOrderStateTransitionException;
import at.itkolleg.statepattern.order.model.KitOrder;

abstract class AbstractOrderState implements OrderState {

    @Override
    public void pay(KitOrder order) {
        throw invalidTransition(order, "bezahlen");
    }

    @Override
    public void pack(KitOrder order) {
        throw invalidTransition(order, "verpacken");
    }

    @Override
    public void ship(KitOrder order, String trackingNumber) {
        throw invalidTransition(order, "versenden");
    }

    @Override
    public void cancel(KitOrder order, String reason) {
        throw invalidTransition(order, "stornieren");
    }

    @Override
    public void returnOrder(KitOrder order, String reason) {
        throw invalidTransition(order, "retournieren");
    }

    protected InvalidOrderStateTransitionException invalidTransition(KitOrder order, String action) {
        return new InvalidOrderStateTransitionException(order.getId(), order.getStatus(), action);
    }

    protected String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " darf nicht leer sein.");
        }
        return value.trim();
    }
}