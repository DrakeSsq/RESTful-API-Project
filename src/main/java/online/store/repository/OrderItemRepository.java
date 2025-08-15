package online.store.repository;

import online.store.dto.OrderItemDto;
import online.store.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    @Query("""
        SELECT NEW online.store.dto.OrderItemDto(
            p.id,
            p.name,
            oi.quantity,
            oi.price
        )
        FROM Order o
        JOIN o.items oi
        JOIN oi.product p
        WHERE o.id = :orderId
    """)
    List<OrderItemDto> findOrderItems(@Param("orderId") UUID orderId);
}
