package online.store.repostitories;

import jakarta.persistence.LockModeType;
import online.store.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с продуктами в базе данных.
 * Наследует стандартные CRUD-операции из JpaRepository.
 * Работает с сущностями Product, используя UUID в качестве идентификатора.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsProductByArticle(String article);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Product p")
    @Transactional
    List<Product> findAllWithPessimisticLock();

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Product p order by p.id")
    @Transactional
    List<Product> findBatchWithOptimisticLock(Pageable pageable);


}
