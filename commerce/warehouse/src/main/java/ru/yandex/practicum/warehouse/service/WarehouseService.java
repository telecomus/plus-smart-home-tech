package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {
    void createProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto getShoppingCart();
}
