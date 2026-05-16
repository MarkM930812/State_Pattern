package at.itkolleg.statepattern.order.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.itkolleg.statepattern.order.dto.CreateKitOrderRequest;
import at.itkolleg.statepattern.order.dto.KitOrderResponse;
import at.itkolleg.statepattern.order.dto.ReasonRequest;
import at.itkolleg.statepattern.order.dto.ShipOrderRequest;
import at.itkolleg.statepattern.order.exception.OrderNotFoundException;
import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;
import at.itkolleg.statepattern.order.repository.KitOrderRepository;
import at.itkolleg.statepattern.order.state.OrderState;

/**
 * Zentrale Orchestrierung im with-pattern-Branch.
 * Anders als im without-pattern-Branch delegiert der Service Statusaktionen an State-Objekte.
 */
@Service
@Transactional
public class KitOrderService {

    private final KitOrderRepository kitOrderRepository;
    private final OrderStateFactory orderStateFactory;

    public KitOrderService(KitOrderRepository kitOrderRepository, OrderStateFactory orderStateFactory) {
        this.kitOrderRepository = kitOrderRepository;
        this.orderStateFactory = orderStateFactory;
    }

    @Transactional(readOnly = true)
    public List<KitOrderResponse> getAllOrders() {
        return kitOrderRepository.findAll()
                .stream()
                .map(KitOrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public KitOrderResponse getOrderById(Long orderId) {
        return KitOrderResponse.from(findOrder(orderId));
    }

    @Transactional(readOnly = true)
    public List<KitOrderResponse> getOrdersByStatus(OrderStatus status) {
        return kitOrderRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(KitOrderResponse::from)
                .toList();
    }

    public KitOrderResponse createOrder(CreateKitOrderRequest request) {
        BigDecimal normalizedUnitPrice = request.unitPrice().setScale(2, RoundingMode.HALF_UP);

        KitOrder order = new KitOrder();
        order.setCustomerName(request.customerName().trim());
        order.setCustomerEmail(request.customerEmail().trim());
        order.setShippingAddress(request.shippingAddress().trim());
        order.setKitName(request.kitName().trim());
        order.setQuantity(request.quantity());
        order.setUnitPrice(normalizedUnitPrice);
        order.setTotalPrice(normalizedUnitPrice.multiply(BigDecimal.valueOf(request.quantity())).setScale(2, RoundingMode.HALF_UP));
        order.setStatus(OrderStatus.CREATED);

        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse pay(Long orderId) {
        KitOrder order = findOrder(orderId);
        // Der Service wählt nur den aktuellen State und delegiert die Fachregel weiter.
        currentState(order).pay(order);
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse pack(Long orderId) {
        KitOrder order = findOrder(orderId);
        currentState(order).pack(order);
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse ship(Long orderId, ShipOrderRequest request) {
        KitOrder order = findOrder(orderId);
        currentState(order).ship(order, request.trackingNumber());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse cancel(Long orderId, ReasonRequest request) {
        KitOrder order = findOrder(orderId);
        currentState(order).cancel(order, request.reason());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse returnOrder(Long orderId, ReasonRequest request) {
        KitOrder order = findOrder(orderId);
        currentState(order).returnOrder(order, request.reason());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    private KitOrder findOrder(Long orderId) {
        return kitOrderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private OrderState currentState(KitOrder order) {
        return orderStateFactory.getState(order.getStatus());
    }
}