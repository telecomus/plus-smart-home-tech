package ru.yandex.practicum.interaction_api.interaction;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction_api.dto.product.ProductCategory;
import ru.yandex.practicum.interaction_api.dto.product.ProductDto;
import ru.yandex.practicum.interaction_api.dto.product.SetProductQuantityStateRequest;

@FeignClient(name = "warehouse")
public interface ShoppingStoreClient {
    @GetMapping("/api/v1/shopping-store")
    Page<ProductDto> getShoppingStore(ProductCategory category, Pageable pageable) throws FeignException;

    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewProduct(@Valid ProductDto productDto) throws FeignException;

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@Valid ProductDto productDto) throws FeignException;

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    Boolean removeProductFromStore(String productId) throws FeignException;

    @PostMapping("/api/v1/shopping-store/quantityState")
    Boolean setQuantityState(@Valid SetProductQuantityStateRequest request) throws FeignException;

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(String productId) throws FeignException;
}
