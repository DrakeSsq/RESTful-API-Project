package online.store.repository;

import online.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o join fetch o.items i join fetch i.product p where o.id = :orderId and o.customer = :customerId")
    Optional<Order> findByIdAndCustomerIdWithItemsAndProducts(@Param("orderId") UUID orderId, @Param("customerId") Long customerId);
}
