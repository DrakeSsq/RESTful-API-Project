package online.store.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import online.store.dto.ProductDto;
import online.store.models.Product;
import online.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления продуктами в интернет-магазине.
 * Предоставляет CRUD-операции для работы с товарами через REST API.
 */
@RestController
@RequestMapping("api/products")
@Tag(name = "ProductController")
public class ProductController {

    private final ProductService productService;

    /**
     * Конструктор с внедрением зависимости сервиса продуктов
     * @param productService сервис для работы с продуктами
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Получает список всех продуктов
     * @return список DTO продуктов и HTTP статус 200 OK
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    /**
     * Создает новый продукт
     * @param productDto DTO продукта для создания
     * @return HTTP статус 201 Created при успешном создании
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        productService.saveProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получает продукт по его идентификатору
     * @param id UUID продукта
     * @return DTO продукта и HTTP статус 200 OK
     */
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") UUID id) {
        ProductDto product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Обновляет существующий продукт
     * @param id UUID продукта для обновления
     * @param newProductDto DTO с новыми данными продукта
     * @return HTTP статус 200 OK при успешном обновлении
     */
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") UUID id,
                                                 @Valid @RequestBody ProductDto newProductDto) {
        productService.updateProduct(id, newProductDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Удаляет продукт по его идентификатору
     * @param id UUID продукта для удаления
     * @return HTTP статус 204 No Content при успешном удалении
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
