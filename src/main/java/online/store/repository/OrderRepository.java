package online.store.repository;

import online.store.dto.OrderInfo;
import online.store.entity.Order;
import online.store.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o join fetch o.items i join fetch i.product p where o.id = :orderId and o.customer = :customerId")
    Optional<Order> findByIdAndCustomerIdWithItemsAndProducts(@Param("orderId") UUID orderId, @Param("customerId") Long customerId);

    @Query("""
        select new online.store.dto.OrderInfo (
            p.id,
            new online.store.dto.CustomerInfo (
                        c.id,
                        c.login,
                        null,
                        c.email,
                        null
            ),
            p.status,
            p.deliveryAddress,
            i.quantity
        )
        from Order p
        join p.customer c
        join p.items i
        where p.status in :statuses
""")
    List<OrderInfo> findAllByStatusIn(@Param("statuses")List<Status> statuses);
}
