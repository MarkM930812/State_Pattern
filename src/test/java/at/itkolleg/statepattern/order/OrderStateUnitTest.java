package at.itkolleg.statepattern.order;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import at.itkolleg.statepattern.order.exception.InvalidOrderStateTransitionException;
import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;
import at.itkolleg.statepattern.order.state.CancelledOrderState;
import at.itkolleg.statepattern.order.state.CreatedOrderState;
import at.itkolleg.statepattern.order.state.PackedOrderState;
import at.itkolleg.statepattern.order.state.PaidOrderState;
import at.itkolleg.statepattern.order.state.ShippedOrderState;

/**
 * Unit-Tests für die konkreten Zustandsklassen.
 * Diese Tests sind ein zentraler Vorteil der Pattern-Lösung, weil Übergänge isoliert geprüft werden können.
 */
class OrderStateUnitTest {

    private final CreatedOrderState createdOrderState = new CreatedOrderState();
    private final PaidOrderState paidOrderState = new PaidOrderState();
    private final PackedOrderState packedOrderState = new PackedOrderState();
    private final ShippedOrderState shippedOrderState = new ShippedOrderState();
    private final CancelledOrderState cancelledOrderState = new CancelledOrderState();

    @Test
    void createdStateShouldMoveToPaid() {
        KitOrder order = sampleOrder(OrderStatus.CREATED);

        createdOrderState.pay(order);

        assertEquals(OrderStatus.PAID, order.getStatus());
        assertNotNull(order.getPaidAt());
    }

    @Test
    void paidStateShouldMoveToPacked() {
        KitOrder order = sampleOrder(OrderStatus.PAID);

        paidOrderState.pack(order);

        assertEquals(OrderStatus.PACKED, order.getStatus());
        assertNotNull(order.getPackedAt());
    }

    @Test
    void packedStateShouldRequireTrackingNumber() {
        KitOrder order = sampleOrder(OrderStatus.PACKED);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> packedOrderState.ship(order, "   ")
        );

        assertEquals("trackingNumber darf nicht leer sein.", exception.getMessage());
    }

    @Test
    void shippedStateShouldMoveToReturned() {
        KitOrder order = sampleOrder(OrderStatus.SHIPPED);

        shippedOrderState.returnOrder(order, "Falsche Größe geliefert");

        assertEquals(OrderStatus.RETURNED, order.getStatus());
        assertEquals("Falsche Größe geliefert", order.getReturnReason());
        assertNotNull(order.getReturnedAt());
    }

    @Test
    void cancelledStateShouldRejectPayAction() {
        KitOrder order = sampleOrder(OrderStatus.CANCELLED);

        InvalidOrderStateTransitionException exception = assertThrows(
                InvalidOrderStateTransitionException.class,
                () -> cancelledOrderState.pay(order)
        );

        assertEquals(
                "Die Aktion 'bezahlen' ist für Bestellung 42 im Status 'CANCELLED' nicht erlaubt.",
                exception.getMessage()
        );
    }

    private KitOrder sampleOrder(OrderStatus status) {
        KitOrder order = new KitOrder();
        order.setId(42L);
        order.setCustomerName("Chris Muster");
        order.setCustomerEmail("chris@example.com");
        order.setShippingAddress("Musterstraße 12");
        order.setKitName("Ajax 1995 Home");
        order.setQuantity(1);
        order.setUnitPrice(new BigDecimal("99.90"));
        order.setTotalPrice(new BigDecimal("99.90"));
        order.setStatus(status);
        return order;
    }
}