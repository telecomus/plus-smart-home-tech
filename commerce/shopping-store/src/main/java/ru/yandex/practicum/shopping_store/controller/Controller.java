package ru.yandex.practicum.shopping_store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction_api.dto.product.ProductCategory;
import ru.yandex.practicum.interaction_api.dto.product.ProductDto;
import ru.yandex.practicum.interaction_api.dto.product.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction_api.interaction.ShoppingStoreClient;
import ru.yandex.practicum.shopping_store.service.ShoppingStoreService;

@RestController
@RequestMapping("${api.path}/shopping-store")
@RequiredArgsConstructor
public class Controller implements ShoppingStoreClient {
    private final ShoppingStoreService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getShoppingStore(@RequestParam ProductCategory category,
                                             Pageable pageable) {
        return service.getShoppingStore(category, pageable);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto createNewProduct(@Valid @RequestBody ProductDto productDto) {
        return service.createNewProduct(productDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProductFromStore(@RequestBody String productId) {
        return service.removeProductFromStore(productId.replaceAll("^\"|\"$", ""));
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public Boolean setQuantityState(@Valid SetProductQuantityStateRequest request) {
        return service.setQuantityState(request);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable String productId) {
        return service.getProduct(productId);
    }
}
