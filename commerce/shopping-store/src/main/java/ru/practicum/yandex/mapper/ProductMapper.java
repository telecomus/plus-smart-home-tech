package ru.practicum.yandex.mapper;

import org.mapstruct.*;
import ru.practicum.yandex.model.Product;
import ru.yandex.practicum.dto.ProductDto;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "rating", constant = "0")
    @Mapping(target = "productId", expression = "java(mapProductId(productDto))")
    Product productDtoToProduct(ProductDto productDto);

    default String mapProductId(ProductDto productDto) {
        if (productDto.getProductId() == null || productDto.getProductId().isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return productDto.getProductId();
    }

    ProductDto productToProductDto(Product product);

    List<ProductDto> mapListProducts(List<Product> products);
}