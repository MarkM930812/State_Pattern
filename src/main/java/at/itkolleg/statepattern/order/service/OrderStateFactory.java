package at.itkolleg.statepattern.order.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import at.itkolleg.statepattern.order.model.OrderStatus;
import at.itkolleg.statepattern.order.state.OrderState;

/**
 * Bildet jeden Status auf genau eine State-Implementierung ab.
 */
@Component
public class OrderStateFactory {

    private final Map<OrderStatus, OrderState> statesByStatus;

    public OrderStateFactory(List<OrderState> states) {
        // Spring injiziert alle OrderState-Beans; daraus bauen wir eine unveränderliche Lookup-Map.
        this.statesByStatus = states.stream()
                .collect(Collectors.toUnmodifiableMap(OrderState::supports, Function.identity()));
    }

    public OrderState getState(OrderStatus status) {
        OrderState state = statesByStatus.get(status);
        if (state == null) {
            throw new IllegalStateException("Kein State für den Status " + status + " registriert.");
        }
        return state;
    }
}