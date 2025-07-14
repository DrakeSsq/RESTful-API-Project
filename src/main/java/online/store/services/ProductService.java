package online.store.services;

import jakarta.validation.Valid;
import online.store.dto.ProductDto;
import online.store.exceptions.ProductNotFoundException;
import online.store.exceptions.UpdateProductException;
import online.store.mappers.ProductMapper;
import online.store.models.Product;
import online.store.repostitories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с продуктами.
 * Обеспечивает бизнес-логику операций CRUD и валидацию данных.
 */
@Service
public class ProductService {

    private final String PRODUCT_NOT_FOUND = "No product with this UUID was found";
    private final String UPDATE_PRODUCT = "Error when changing the product. Check the correctness of the entered data";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Конструктор с внедрением зависимостей
     * @param productRepository репозиторий для работы с продуктами
     * @param productMapper маппер для преобразования DTO/Entity
     */
    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Сохраняет новый продукт
     * @param productDto DTO продукта для сохранения
     */
    public void saveProduct(ProductDto productDto) {

        Product product = productMapper.toEntity(productDto);
        if (!productDto.getQuantity().equals(product.getQuantity())) {
            product.setLastQuantityUpdatedAt(LocalDateTime.now());
        }
        productRepository.save(product);
    }

    /**
     * Получает список всех продуктов
     * @return список DTO продуктов
     */
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream().map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Находит продукт по идентификатору
     * @param id UUID продукта
     * @return DTO продукта
     * @throws ProductNotFoundException если продукт не найден
     */
    public ProductDto getProductById(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(productMapper::toDto).orElseThrow(
                () -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

    /**
     * Удаляет продукт по идентификатору
     * @param id UUID продукта для удаления
     * @throws ProductNotFoundException если продукт не найден
     */
    public void deleteProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        productRepository.delete(product);
    }

    /**
     * Обновляет данные продукта
     * @param id UUID продукта для обновления
     * @param newProductDto DTO с новыми данными продукта
     * @throws ProductNotFoundException если продукт не найден
     * @throws UpdateProductException при ошибке обновления
     */
    public void updateProduct(UUID id, @Valid ProductDto newProductDto) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        productRepository.findById(id).map(
                existingProduct -> {
                    productMapper.updateEntityFromDto(newProductDto, existingProduct);
                    if (!existingProduct.getQuantity().equals(newProductDto.getQuantity())) {
                        existingProduct.setLastQuantityUpdatedAt(LocalDateTime.now());
                    }

                    Product product = productRepository.save(existingProduct);
                    return productMapper.toDto(product);
                }
        ).orElseThrow(() -> new UpdateProductException(UPDATE_PRODUCT));
    }
}
