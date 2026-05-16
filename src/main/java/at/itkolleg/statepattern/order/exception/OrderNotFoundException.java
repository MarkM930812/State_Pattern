package at.itkolleg.statepattern.order.exception;

/**
 * Wird ausgelöst, wenn eine Bestellung per ID nicht gefunden wird.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Bestellung mit der ID " + orderId + " wurde nicht gefunden.");
    }
}