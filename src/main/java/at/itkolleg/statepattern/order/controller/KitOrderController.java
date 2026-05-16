package at.itkolleg.statepattern.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.itkolleg.statepattern.order.dto.CreateKitOrderRequest;
import at.itkolleg.statepattern.order.dto.KitOrderResponse;
import at.itkolleg.statepattern.order.dto.ReasonRequest;
import at.itkolleg.statepattern.order.dto.ShipOrderRequest;
import at.itkolleg.statepattern.order.model.OrderStatus;
import at.itkolleg.statepattern.order.service.KitOrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class KitOrderController {

    private final KitOrderService kitOrderService;

    public KitOrderController(KitOrderService kitOrderService) {
        this.kitOrderService = kitOrderService;
    }

    @GetMapping
    public List<KitOrderResponse> getAllOrders() {
        return kitOrderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public KitOrderResponse getOrderById(@PathVariable Long orderId) {
        return kitOrderService.getOrderById(orderId);
    }

    @GetMapping("/status/{status}")
    public List<KitOrderResponse> getOrdersByStatus(@PathVariable OrderStatus status) {
        return kitOrderService.getOrdersByStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KitOrderResponse createOrder(@Valid @RequestBody CreateKitOrderRequest request) {
        return kitOrderService.createOrder(request);
    }

    @PostMapping("/{orderId}/pay")
    public KitOrderResponse pay(@PathVariable Long orderId) {
        return kitOrderService.pay(orderId);
    }

    @PostMapping("/{orderId}/pack")
    public KitOrderResponse pack(@PathVariable Long orderId) {
        return kitOrderService.pack(orderId);
    }

    @PostMapping("/{orderId}/ship")
    public KitOrderResponse ship(@PathVariable Long orderId, @Valid @RequestBody ShipOrderRequest request) {
        return kitOrderService.ship(orderId, request);
    }

    @PostMapping("/{orderId}/cancel")
    public KitOrderResponse cancel(@PathVariable Long orderId, @Valid @RequestBody ReasonRequest request) {
        return kitOrderService.cancel(orderId, request);
    }

    @PostMapping("/{orderId}/return")
    public KitOrderResponse returnOrder(@PathVariable Long orderId, @Valid @RequestBody ReasonRequest request) {
        return kitOrderService.returnOrder(orderId, request);
    }
}