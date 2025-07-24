package ru.practicum.yandex.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.model.Product;
import ru.yandex.practicum.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();

        // Если productId пустой или null, сгенерируем новый
        if (productDto.getProductId() == null || productDto.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        } else {
            product.setProductId(productDto.getProductId());
        }

        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setQuantityState(productDto.getQuantityState());
        product.setProductState(productDto.getProductState());
        product.setRating(0); // Устанавливаем значение по умолчанию
        product.setProductCategory(productDto.getProductCategory());
        product.setPrice(productDto.getPrice());

        return product;
    }

    @Override
    public ProductDto productToProductDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setDescription(product.getDescription());
        productDto.setImageSrc(product.getImageSrc());
        productDto.setQuantityState(product.getQuantityState());
        productDto.setProductState(product.getProductState());
        productDto.setProductCategory(product.getProductCategory());
        productDto.setPrice(product.getPrice());

        return productDto;
    }

    @Override
    public List<ProductDto> mapListProducts(List<Product> products) {
        if (products == null) {
            return null;
        }

        List<ProductDto> list = new ArrayList<>(products.size());
        for (Product product : products) {
            list.add(productToProductDto(product));
        }

        return list;
    }
}