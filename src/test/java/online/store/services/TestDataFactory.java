package online.store.services;

import online.store.dto.ProductDto;
import online.store.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataFactory {

    public static final UUID productId = UUID.randomUUID();

    public static Product getProduct() {
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setArticle("TEST-001");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setQuantity(10);
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    public static ProductDto getProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setArticle("TEST-001");
        productDto.setPrice(BigDecimal.valueOf(100.00));
        productDto.setQuantity(10);
        return productDto;
    }
}
