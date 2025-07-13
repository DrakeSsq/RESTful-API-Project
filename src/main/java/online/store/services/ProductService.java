package online.store.services;

import jakarta.validation.Valid;
import online.store.dto.ProductDto;
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

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void saveProduct(ProductDto productDto) {

        Product product = productMapper.toEntity(productDto);
        System.out.println("SERVICE - " + product.getName());

        if (!productDto.getQuantity().equals(product.getQuantity())) {
            product.setLastQuantityUpdatedAt(LocalDateTime.now());
        }

        productRepository.save(product);
    }

    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream().map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(productMapper::toDto).orElse(null);
    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    public ProductDto updateProduct(UUID id, @Valid ProductDto newProductDto) {
        return productRepository.findById(id).map(
                existingProduct -> {
                    productMapper.updateEntityFromDto(newProductDto, existingProduct);
                    if (!existingProduct.getQuantity().equals(newProductDto.getQuantity())) {
                        existingProduct.setLastQuantityUpdatedAt(LocalDateTime.now());
                    }

                    Product product = productRepository.save(existingProduct);
                    return productMapper.toDto(product);
                }
        ).orElse(null);
    }
}
