package online.store.service;

import jakarta.validation.Valid;
import online.store.criteria.SearchCriterion;
import online.store.dto.ProductDto;
import online.store.exception.ProductNotFoundException;
import online.store.exception.UpdateProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с продуктами.
 */
public interface ProductService {

    /**
     * Сохраняет новый продукт
     * @param productDto DTO продукта для сохранения
     */
    UUID createProduct(ProductDto productDto);

    /**
     * Получает список всех продуктов
     * @param pageable параметр для пагинации
     * @return список DTO продуктов
     */
    Page<ProductDto> getAll(Pageable pageable);

    /**
     * Находит продукт по идентификатору
     * @param id UUID продукта
     * @return DTO продукта
     * @throws ProductNotFoundException если продукт не найден
     */
    ProductDto getProductById(UUID id);

    /**
     * Удаляет продукт по идентификатору
     * @param id UUID продукта для удаления
     * @throws ProductNotFoundException если продукт не найден
     */
    void deleteProductById(UUID id);

    /**
     * Обновляет данные продукта
     * @param id UUID продукта для обновления
     * @param newProductDto DTO с новыми данными продукта
     * @throws ProductNotFoundException если продукт не найден
     * @throws UpdateProductException при ошибке обновления
     */
    ProductDto updateProduct(UUID id, @Valid ProductDto newProductDto);

    /**
     * Производит поиск объектов по критериям
     * @param criteria критерии для поиска
     * @param pageable параметр для пагинации
     * @return найденных объектов, подходящих по критериям
     */
    Page<ProductDto> searchAll(List<SearchCriterion> criteria, Pageable pageable);

    /**
     * Создает объекты из передаваемого списка
     * @param list список объектов
     */
    void createAllProducts(List<ProductDto> list);
}
