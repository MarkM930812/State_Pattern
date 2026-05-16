package at.itkolleg.statepattern.order.exception;

import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * Wird verwendet, wenn eine fachliche Aktion im aktuellen Bestellstatus nicht erlaubt ist.
 */
public class InvalidOrderStateTransitionException extends RuntimeException {

    public InvalidOrderStateTransitionException(Long orderId, OrderStatus status, String action) {
        super("Die Aktion '" + action + "' ist für Bestellung " + orderId + " im Status '" + status + "' nicht erlaubt.");
    }
}