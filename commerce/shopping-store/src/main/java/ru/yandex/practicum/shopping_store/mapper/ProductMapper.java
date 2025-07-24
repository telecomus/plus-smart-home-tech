package ru.yandex.practicum.shopping_store.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction_api.dto.product.ProductDto;
import ru.yandex.practicum.shopping_store.model.Product;

@Component
public class ProductMapper {
    public Product fromProductDto(ProductDto productDto) {
        return Product.builder()
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    public ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId().toString())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }
}
