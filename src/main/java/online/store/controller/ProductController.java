package online.store.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import online.store.criteria.SearchCriterion;
import online.store.dto.ProductDto;
import online.store.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления продуктами в интернет-магазине.
 * Предоставляет CRUD-операции для работы с товарами через REST API.
 */
@RequestMapping("api/products")
@Tag(name = "Product management API")
public interface ProductController {

    /**
     * Создает новый продукт
     * @param product DTO продукта для создания
     * @return HTTP статус 201 Created при успешном создании
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseDto<UUID> createProduct(@RequestBody @Valid ProductDto product);

    /**
     * Получает продукт по его идентификатору
     * @param id UUID продукта
     * @return DTO продукта и HTTP статус 200 OK
     */
    @GetMapping("/{id}")
    ResponseDto<ProductDto> getProductById(@PathVariable("id") UUID id);

    /**
     * Получает список всех продуктов
     * @param pageable параметр для пагинации
     * @return список DTO продуктов
     */
    @GetMapping
    Page<ProductDto> getAll(Pageable pageable);

    /**
     * Обновляет существующий продукт
     * @param id UUID продукта для обновления
     * @param product DTO с новыми данными продукта
     * @return HTTP статус 200 OK при успешном обновлении
     */
    @PutMapping("/{id}")
    ResponseDto<ProductDto> updateProduct(@PathVariable("id") UUID id, @RequestBody @Valid ProductDto product);

    /**
     * Удаляет продукт по его идентификатору
     * @param id UUID продукта для удаления
     * @return HTTP статус 204 No Content при успешном удалении
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseDto<Void> deleteProduct(@PathVariable("id") UUID id);

    /**
     * Задает фильтры для вывода данных
     * @param criteria критерии для поиска
     * @param pageable параметр для пагинации
     * @return Отфильтрованный список
     */
    @PostMapping("/search")
    Page<ProductDto> searchAll(@Valid @RequestBody List<SearchCriterion> criteria, Pageable pageable);

}
