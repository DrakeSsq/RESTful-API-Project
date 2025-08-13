package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.criteria.SearchCriterion;
import online.store.criteria.specification.MySpecificationBuilder;
import online.store.dto.ProductDto;
import online.store.exception.ArticleAlreadyExistsException;
import online.store.exception.ProductNotFoundException;
import online.store.exception.UpdateProductException;
import online.store.mapper.ProductMapper;
import online.store.entity.Product;
import online.store.repostitory.ProductRepository;
import online.store.service.ProductService;
import online.store.util.LogMessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static online.store.util.ProductServiceUtil.*;
import static online.store.util.LogMessageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final MySpecificationBuilder<Product> specificationBuilder;

    @Override
    @Transactional
    public UUID createProduct(ProductDto productDto) {

        Product product = productMapper.toEntity(productDto);

        log.info(CHECK_ARTICLE);
        validateArticleAvailability(product.getArticle());

        log.info(SAVING_IN_DB_LOG, product);
        productRepository.save(product);

        return product.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAll(Pageable pageable) {

        log.info(GETTING_PAGES, pageable.getOffset(), pageable.getPageNumber());

        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));

        log.info(GETTING_PRODUCT, id);

        return productMapper.toDto(product);

    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));

        productRepository.delete(product);
        log.info(DELETE_PRODUCT, id);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(UUID id, @Validated ProductDto newProductDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new UpdateProductException(String.format(PRODUCT_NOT_FOUND, id)));

        if (!product.getQuantity().equals(newProductDto.getQuantity())) {
            product.setLastQuantityUpdatedAt(LocalDateTime.now());
        }

        productRepository.save(productMapper.updateEntityFromDto(newProductDto, product));
        log.info(LogMessageUtil.UPDATE_PRODUCT, product);

        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> searchAll(List<SearchCriterion> criteria, Pageable pageable) {
        if (criteria == null || criteria.isEmpty()) {
            return productRepository.findAll(pageable).map(productMapper::toDto);
        }
        Specification<Product> spec = specificationBuilder.build(Product.class, criteria);
        return productRepository.findAll(spec, pageable).map(productMapper::toDto);

    }

    @Override
    @Transactional
    public void createAllProducts(List<ProductDto> list) {
        for (ProductDto productDto : list) {
            createProduct(productDto);
        }
    }

    private void validateArticleAvailability(String article) {
        if (productRepository.existsProductByArticle(article)) {
            throw new ArticleAlreadyExistsException(String.format(ARTICLE_ALREADY_EXISTS, article));
        }
    }
}
