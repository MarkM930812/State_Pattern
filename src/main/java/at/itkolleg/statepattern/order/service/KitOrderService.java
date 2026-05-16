package at.itkolleg.statepattern.order.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.itkolleg.statepattern.order.dto.CreateKitOrderRequest;
import at.itkolleg.statepattern.order.dto.KitOrderResponse;
import at.itkolleg.statepattern.order.dto.ReasonRequest;
import at.itkolleg.statepattern.order.dto.ShipOrderRequest;
import at.itkolleg.statepattern.order.exception.InvalidOrderStateTransitionException;
import at.itkolleg.statepattern.order.exception.OrderNotFoundException;
import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;
import at.itkolleg.statepattern.order.repository.KitOrderRepository;

@Service
@Transactional
public class KitOrderService {

    private final KitOrderRepository kitOrderRepository;

    public KitOrderService(KitOrderRepository kitOrderRepository) {
        this.kitOrderRepository = kitOrderRepository;
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
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateTransitionException(order.getId(), order.getStatus(), "bezahlen");
        }

        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse pack(Long orderId) {
        KitOrder order = findOrder(orderId);
        if (order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStateTransitionException(order.getId(), order.getStatus(), "verpacken");
        }

        order.setStatus(OrderStatus.PACKED);
        order.setPackedAt(LocalDateTime.now());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse ship(Long orderId, ShipOrderRequest request) {
        KitOrder order = findOrder(orderId);
        if (order.getStatus() != OrderStatus.PACKED) {
            throw new InvalidOrderStateTransitionException(order.getId(), order.getStatus(), "versenden");
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setTrackingNumber(requireText(request.trackingNumber(), "trackingNumber"));
        order.setShippedAt(LocalDateTime.now());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse cancel(Long orderId, ReasonRequest request) {
        KitOrder order = findOrder(orderId);

        if (order.getStatus() != OrderStatus.CREATED
                && order.getStatus() != OrderStatus.PAID
                && order.getStatus() != OrderStatus.PACKED) {
            throw new InvalidOrderStateTransitionException(order.getId(), order.getStatus(), "stornieren");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(requireText(request.reason(), "reason"));
        order.setCancelledAt(LocalDateTime.now());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    public KitOrderResponse returnOrder(Long orderId, ReasonRequest request) {
        KitOrder order = findOrder(orderId);

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new InvalidOrderStateTransitionException(order.getId(), order.getStatus(), "retournieren");
        }

        order.setStatus(OrderStatus.RETURNED);
        order.setReturnReason(requireText(request.reason(), "reason"));
        order.setReturnedAt(LocalDateTime.now());
        return KitOrderResponse.from(kitOrderRepository.save(order));
    }

    private KitOrder findOrder(Long orderId) {
        return kitOrderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " darf nicht leer sein.");
        }
        return value.trim();
    }
}