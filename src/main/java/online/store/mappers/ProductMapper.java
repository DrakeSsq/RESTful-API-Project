package online.store.mappers;

import online.store.dto.ProductDto;
import online.store.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastQuantityUpdatedAt", ignore = true)
    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastQuantityUpdatedAt", ignore = true)
    void updateEntityFromDto(ProductDto productDto, @MappingTarget Product product);
}
