package at.itkolleg.statepattern.order;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.itkolleg.statepattern.order.dto.CreateKitOrderRequest;
import at.itkolleg.statepattern.order.dto.ReasonRequest;
import at.itkolleg.statepattern.order.dto.ShipOrderRequest;
import at.itkolleg.statepattern.order.repository.KitOrderRepository;

/**
 * Integrationstests für die REST-Endpunkte des without-pattern-Branches.
 * Die Tests prüfen die gesamte Kette aus Controller, Service, Repository und Fehlerbehandlung.
 */
@SpringBootTest
@AutoConfigureMockMvc
class KitOrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KitOrderRepository kitOrderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
                // Jeder Test startet mit einer leeren In-Memory-Datenbank.
        kitOrderRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderInCreatedState() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Chris Muster"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.totalPrice").value(179.98));
    }

    @Test
    void shouldPayAndPackOrder() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(post("/api/orders/{orderId}/pay", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        mockMvc.perform(post("/api/orders/{orderId}/pack", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PACKED"));
    }

    @Test
    void shouldShipPackedOrder() throws Exception {
        Long orderId = createPreparedOrder();

        mockMvc.perform(post("/api/orders/{orderId}/ship", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShipOrderRequest("TRACK-2026-001"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"))
                .andExpect(jsonPath("$.trackingNumber").value("TRACK-2026-001"));
    }

    @Test
    void shouldCancelCreatedOrder() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(post("/api/orders/{orderId}/cancel", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasonRequest("Kunde hat sich umentschieden"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.cancellationReason").value("Kunde hat sich umentschieden"));
    }

    @Test
    void shouldReturnShippedOrder() throws Exception {
        Long orderId = createPreparedOrder();

        mockMvc.perform(post("/api/orders/{orderId}/ship", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShipOrderRequest("TRACK-2026-002"))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/orders/{orderId}/return", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasonRequest("Größe passt nicht"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"))
                .andExpect(jsonPath("$.returnReason").value("Größe passt nicht"));
    }

    @Test
    void shouldRejectShippingInCreatedState() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(post("/api/orders/{orderId}/ship", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShipOrderRequest("TRACK-FAIL"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Die Aktion 'versenden'")));
    }

    @Test
    void shouldReturnOrdersByStatus() throws Exception {
        Long orderId = createPreparedOrder();
        mockMvc.perform(post("/api/orders/{orderId}/ship", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShipOrderRequest("TRACK-2026-003"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/orders/status/SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SHIPPED"));
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "",
                                  "customerEmail": "not-an-email",
                                  "shippingAddress": "",
                                  "kitName": "",
                                  "quantity": 0,
                                  "unitPrice": -5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validierungsfehler in der Anfrage."));
    }

    @Test
    void shouldReturn404ForMissingOrder() throws Exception {
        mockMvc.perform(get("/api/orders/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Bestellung mit der ID 99999 wurde nicht gefunden."));
    }

    private Long createOrder() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    private Long createPreparedOrder() throws Exception {
                // Hilfsmethode für Tests, die einen weiter fortgeschrittenen Status benötigen.
        Long orderId = createOrder();
        mockMvc.perform(post("/api/orders/{orderId}/pay", orderId))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/orders/{orderId}/pack", orderId))
                .andExpect(status().isOk());
        return orderId;
    }

    private CreateKitOrderRequest validCreateRequest() {
        return new CreateKitOrderRequest(
                "Chris Muster",
                "chris@example.com",
                "Musterstraße 12, 8020 Graz",
                "AC Milan 2007 Home",
                2,
                new BigDecimal("89.99")
        );
    }
}