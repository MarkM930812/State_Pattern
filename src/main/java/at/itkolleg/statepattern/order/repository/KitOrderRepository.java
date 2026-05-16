package at.itkolleg.statepattern.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import at.itkolleg.statepattern.order.model.KitOrder;
import at.itkolleg.statepattern.order.model.OrderStatus;

/**
 * Repository für Datenbankzugriffe auf Bestellungen.
 */
public interface KitOrderRepository extends JpaRepository<KitOrder, Long> {

    List<KitOrder> findByStatusOrderByCreatedAtDesc(OrderStatus status);
}