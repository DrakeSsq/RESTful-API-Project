package online.store.services;

import online.store.dto.ProductDto;
import online.store.exceptions.ProductNotFoundException;
import online.store.mappers.ProductMapper;
import online.store.models.Product;
import online.store.repostitories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private final UUID productId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setArticle("TEST-001");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setQuantity(10);
        product.setCreatedAt(LocalDateTime.now());

        productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setArticle("TEST-001");
        productDto.setPrice(BigDecimal.valueOf(100.00));
        productDto.setQuantity(10);
    }

    @Test
    void saveProduct_ShouldSaveProduct_WhenValidInput() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        productService.saveProduct(productDto);

        verify(productRepository).save(product);
        verify(productMapper).toEntity(productDto);
    }

    @Test
    void saveProduct_ShouldUpdateLastQuantityUpdatedAt_WhenQuantityChanges() {
        productDto.setQuantity(15);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        productService.saveProduct(productDto);

        assertNotNull(product.getLastQuantityUpdatedAt());
    }

    @Test
    void getAll_ShouldReturnAllProducts() {
        List<Product> products = Arrays.asList(product, new Product());
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        List<ProductDto> productsDto = productService.getAll();

        assertEquals(2, productsDto.size());
        verify(productRepository).findAll();
        verify(productMapper, times(2)).toDto(any(Product.class));
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductDto> productsDto = productService.getAll();

        assertTrue(productsDto.isEmpty());
        verify(productRepository).findAll();
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
        ProductDto updatedDto = new ProductDto();
        updatedDto.setName("Updated Name");
        updatedDto.setQuantity(20);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(updatedDto);

        productService.updateProduct(productId, updatedDto);

        verify(productRepository, times(2)).findById(productId);
        verify(productRepository).save(product);
        assertNotNull(product.getLastQuantityUpdatedAt());
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProduct(productId, productDto));
        verify(productRepository).findById(productId);
    }
}
