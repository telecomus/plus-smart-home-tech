package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction_api.interaction.WarehouseClient;
import ru.yandex.practicum.warehouse.service.WarehouseService;

@RestController
@RequestMapping("${api.path}/warehouse")
@RequiredArgsConstructor
public class Controller implements WarehouseClient {
    private final WarehouseService service;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void createProduct(@Valid @RequestBody NewProductInWarehouseRequest request) {
        service.createProduct(request);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkQuantity(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return service.checkQuantity(shoppingCartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProduct(@Valid @RequestBody AddProductToWarehouseRequest request) {
        service.addProduct(request);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getShoppingCart() {
        return service.getShoppingCart();
    }
}
