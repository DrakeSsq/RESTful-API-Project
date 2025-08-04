package online.store.mapper;

import online.store.dto.ProductDto;
import online.store.entity.Product;
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
    Product updateEntityFromDto(ProductDto productDto, @MappingTarget Product product);
}
