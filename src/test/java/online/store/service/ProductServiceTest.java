package online.store.service;

import online.store.criteria.NumericSearchCriterion;
import online.store.criteria.SearchCriterion;
import online.store.criteria.StringSearchCriterion;
import online.store.criteria.enums.SearchOperation;
import online.store.criteria.specification.ProductSpecificationBuilder;
import online.store.dto.ProductDto;
import online.store.entity.Product;
import online.store.entity.enums.Category;
import online.store.mapper.ProductMapper;
import online.store.repostitory.ProductRepository;
import online.store.service.impl.ProductServiceImpl;
import online.store.util.ProductServiceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static online.store.util.ProductServiceData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(15000));
        product.setCategory(Category.ELECTRONICS);
        product.setArticle("ART123");

        productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(BigDecimal.valueOf(15000));
        productDto.setCategory(Category.ELECTRONICS);
        productDto.setArticle("ART123");
    }

    @Test
    void priceSearch() {
        NumericSearchCriterion criterion = getNumericSearchCriterion();
        List<SearchCriterion> list = Collections.singletonList(criterion);

        when(productRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(product));
        when(productMapper.toDto(any())).thenReturn(productDto);

        List<ProductDto> productDtoList = productService.searchAll(list);

        assertEquals(1, productDtoList.size());
    }

    @Test
    void searchByMultiplyCriteria() {
        NumericSearchCriterion criterion1 = getNumericSearchCriterion();
        StringSearchCriterion criterion2 = getStringSearchCriterion();

        List<SearchCriterion> list = Arrays.asList(criterion1, criterion2);

        when(productRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(product));
        when(productMapper.toDto(any())).thenReturn(productDto);

        List<ProductDto> productDtoList = productService.searchAll(list);

        assertEquals(1, productDtoList.size());
    }

    @Test
    void paginationSearch() {
        int offset = 0;
        int limit = 10;

        Page<Product> page = new PageImpl<>(Collections.singletonList(product),
                PageRequest.of(offset, limit), 1);

        when(productRepository.findAll(PageRequest.of(offset, limit))).thenReturn(page);
        when(productMapper.toDto(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getAll(offset, limit);

        assertEquals(1, result.size());
        assertEquals(productDto, result.get(0));
        verify(productRepository).findAll(PageRequest.of(offset, limit));
    }
    @Test
    void searchAllWhenNoProductsFound() {
        NumericSearchCriterion priceCriterion = getSecondNumericSearchCriterion();

        List<SearchCriterion> criteria = Collections.singletonList(priceCriterion);

        when(productRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<ProductDto> result = productService.searchAll(criteria);

        assertTrue(result.isEmpty());
    }

    @Test
    void searchAllWithoutCriteria() {
        List<SearchCriterion> criteria = Collections.emptyList();

        List<Product> allProducts = Arrays.asList(product, new Product());
        when(productRepository.findAll()).thenReturn(allProducts);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        List<ProductDto> result = productService.searchAll(criteria);

        assertEquals(2, result.size());
    }

}
