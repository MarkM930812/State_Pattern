package at.itkolleg.statepattern.order.model;

/**
 * Alle fachlichen Zustände, die eine Bestellung im Projekt annehmen kann.
 */
public enum OrderStatus {
    CREATED,
    PAID,
    PACKED,
    SHIPPED,
    CANCELLED,
    RETURNED
}