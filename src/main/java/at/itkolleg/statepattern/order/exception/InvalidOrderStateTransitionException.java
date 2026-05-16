package at.itkolleg.statepattern.order.exception;

import at.itkolleg.statepattern.order.model.OrderStatus;

public class InvalidOrderStateTransitionException extends RuntimeException {

    public InvalidOrderStateTransitionException(Long orderId, OrderStatus status, String action) {
        super("Die Aktion '" + action + "' ist für Bestellung " + orderId + " im Status '" + status + "' nicht erlaubt.");
    }
}