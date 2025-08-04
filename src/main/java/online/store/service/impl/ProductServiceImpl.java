package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.dto.ProductDto;
import online.store.exception.ArticleAlreadyExistsException;
import online.store.exception.ProductNotFoundException;
import online.store.exception.UpdateProductException;
import online.store.mapper.ProductMapper;
import online.store.entity.Product;
import online.store.repostitory.ProductRepository;
import online.store.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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

    @Override
    public UUID createProduct(ProductDto productDto) {

        Product product = productMapper.toEntity(productDto);

        log.info("Checking product article availability");
        validateArticleAvailability(product.getArticle());

        log.info(SAVING_IN_DB_LOG, product);
        productRepository.save(product);

        return product.getId();
    }

    @Override
    public List<ProductDto> getAll(Integer offset, Integer limit) {

        log.info("Getting {} pages of {} products", offset, limit);

        return productRepository.findAll(PageRequest.of(offset, limit))
                .map(productMapper::toDto)
                .getContent();
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));

        log.info("Getting a product by UUID {}", id);

        return productMapper.toDto(product);

    }

    @Override
    public void deleteProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));

        productRepository.delete(product);
        log.info("Product with id {} has been deleted", id);
    }

    @Override
    public ProductDto updateProduct(UUID id, @Validated ProductDto newProductDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new UpdateProductException(String.format(PRODUCT_NOT_FOUND, id)));

        if (!product.getQuantity().equals(newProductDto.getQuantity())) {
            product.setLastQuantityUpdatedAt(LocalDateTime.now());
        }

        productRepository.save(productMapper.updateEntityFromDto(newProductDto, product));
        log.info("Product {} has been updated", product);

        return productMapper.toDto(product);
    }

    private void validateArticleAvailability(String article) {
        if (productRepository.existsProductByArticle(article)) {
            throw new ArticleAlreadyExistsException(String.format(ARTICLE_ALREADY_EXISTS, article));
        }
    }
}
