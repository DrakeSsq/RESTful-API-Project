package online.store.repostitories;

import online.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с продуктами в базе данных.
 * Наследует стандартные CRUD-операции из JpaRepository.
 * Работает с сущностями Product, используя UUID в качестве идентификатора.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsProductByArticle(String article);
}
