package at.itkolleg.statepattern.order.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Bestellung mit der ID " + orderId + " wurde nicht gefunden.");
    }
}