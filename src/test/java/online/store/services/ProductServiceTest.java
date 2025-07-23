package online.store.services;

import online.store.dto.ProductDto;
import online.store.exceptions.ProductNotFoundException;
import online.store.exceptions.UpdateProductException;
import online.store.mappers.ProductMapper;
import online.store.entity.Product;
import online.store.repostitories.ProductRepository;
import online.store.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private final Product product = TestDataFactory.getProduct();
    private final ProductDto productDto = TestDataFactory.getProductDto();
    private final UUID productId = TestDataFactory.productId;

    @Test
    void saveProduct_ShouldSaveProduct_WhenValidInput() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        productService.createProduct(productDto);

        verify(productRepository).save(product);
        verify(productMapper).toEntity(productDto);
    }

    @Test
    void saveProduct_ShouldUpdateLastQuantityUpdatedAt_WhenQuantityChanges() {
        productDto.setQuantity(15);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        productService.createProduct(productDto);

        assertNotNull(product.getLastQuantityUpdatedAt());
    }

    @Test
    void getAll_ShouldReturnAllProducts() {
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product, product2);

        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        List<ProductDto> result = productService.getAll(0, 10);

        assertEquals(2, result.size());
        verify(productRepository).findAll(any(Pageable.class));
        verify(productMapper, times(2)).toDto(any(Product.class));
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoProducts() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());

        when(productRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        List<ProductDto> productsDto = productService.getAll(0, 1);

        assertTrue(productsDto.isEmpty());
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(result.getName(), productDto.getName());
        verify(productRepository).findById(productId);
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(productId));
        verify(productRepository).findById(productId);
    }

    @Test
    void deleteProductById_ShouldDeleteProduct_WhenExists() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProductById(productId);

        verify(productRepository).delete(product);
        verify(productRepository).findById(productId);
    }

    @Test
    void deleteProductById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProductById(productId));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenValidInput() {
        UUID testProductId = UUID.fromString("5b915261-74dc-4ec3-880d-013c85601d4a"); // Используем тот же UUID, что и в ошибке
        Product testProduct = new Product();
        testProduct.setId(testProductId);

        ProductDto updatedDto = new ProductDto();
        updatedDto.setName("Updated Name");
        updatedDto.setQuantity(20);

        when(productRepository.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(productMapper.toDto(testProduct)).thenReturn(updatedDto);

        ProductDto result = productService.updateProduct(testProductId, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(UpdateProductException.class, () ->
                productService.updateProduct(productId, productDto));
        verify(productRepository).findById(productId);
    }
}
