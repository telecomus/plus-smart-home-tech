package ru.yandex.practicum.shopping_cart.service;

import ru.yandex.practicum.interaction_api.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addToShoppingCart(String username, Map<String, Long> products);

    void deactivateShoppingCart(String username);

    ShoppingCartDto removeFromShoppingCart(String username, List<String> products);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request);
}
