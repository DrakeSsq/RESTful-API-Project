package online.store.repostitory;

import jakarta.persistence.LockModeType;
import online.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с продуктами в базе данных.
 * Наследует стандартные CRUD-операции из JpaRepository.
 * Работает с сущностями Product, используя UUID в качестве идентификатора.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsProductByArticle(String article);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p")
    Page<Product> findAllPessimistic(Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Product p order by p.id")
    Page<Product> findProductsOptimistic(Pageable pageable);
}
