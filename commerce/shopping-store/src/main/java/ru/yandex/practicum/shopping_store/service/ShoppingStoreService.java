package ru.yandex.practicum.shopping_store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction_api.dto.product.ProductCategory;
import ru.yandex.practicum.interaction_api.dto.product.ProductDto;
import ru.yandex.practicum.interaction_api.dto.product.SetProductQuantityStateRequest;

public interface ShoppingStoreService {
    Page<ProductDto> getShoppingStore(ProductCategory category, Pageable pageable);

    ProductDto createNewProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProductFromStore(String productId);

    Boolean setQuantityState(SetProductQuantityStateRequest request);

    ProductDto getProduct(String productId);
}
