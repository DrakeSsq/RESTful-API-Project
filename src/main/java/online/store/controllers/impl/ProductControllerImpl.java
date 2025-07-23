package online.store.controllers.impl;

import lombok.RequiredArgsConstructor;
import online.store.controllers.ProductController;
import online.store.dto.ProductDto;
import online.store.response.PageResponseDto;
import online.store.response.ResponseDto;
import online.store.services.ProductService;
import org.springframework.web.bind.annotation.*;

import static online.store.util.ResponseMessageUtil.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    public PageResponseDto getAll(Integer offset, Integer limit) {
        return PageResponseDto.builder()
                .content(productService.getAll(offset, limit))
                .offset(offset)
                .limit(limit)
                .build();
    }

    @Override
    public ResponseDto<UUID> createProduct(ProductDto productDto) {
        return ResponseDto.<UUID>builder()
                .message(PRODUCT_CREATED)
                .data(productService.createProduct(productDto))
                .build();
    }

    @Override
    public ResponseDto<ProductDto> getProductById(UUID id) {
        return ResponseDto.<ProductDto>builder()
                .message(PRODUCT_FETCHED_BY_ID)
                .data(productService.getProductById(id))
                .build();
    }

    @Override
    public ResponseDto<ProductDto> updateProduct(UUID id, ProductDto product) {
        return ResponseDto.<ProductDto>builder()
                .message(PRODUCT_UPDATED)
                .data(productService.updateProduct(id, product))
                .build();
    }

    @Override
    public ResponseDto<Void> deleteProduct(UUID id) {
        productService.deleteProductById(id);
        return ResponseDto.<Void>builder()
                .message(PRODUCT_DELETED)
                .build();
    }

}
