package at.itkolleg.statepattern.order.state;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * Gemeinsames Interface aller Zustandsklassen.
 * Jede Implementierung entscheidet, welche Aktionen in ihrem Status erlaubt sind.
 */
public interface OrderState {

    OrderStatus supports();

    void pay(KitOrder order);

    void pack(KitOrder order);

    void ship(KitOrder order, String trackingNumber);

    void cancel(KitOrder order, String reason);

    void returnOrder(KitOrder order, String reason);
}