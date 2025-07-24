package ru.practicum.yandex.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.yandex.model.Product;
import ru.yandex.practicum.dto.ProductDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    default Product productDtoToProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setQuantityState(productDto.getQuantityState());
        product.setProductState(productDto.getProductState());
        product.setRating(0); // Устанавливаем какое-то значение по умолчанию для рейтинга
        product.setProductCategory(productDto.getProductCategory());
        product.setPrice(productDto.getPrice());

        return product;
    }

    ProductDto productToProductDto(Product product);

    List<ProductDto> mapListProducts(List<Product> products);
}