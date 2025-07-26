package ru.yandex.practicum.shopping_cart.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.shopping_cart.model.ShoppingCart;

@Component
public class ShoppingCartMapper {
    public ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(shoppingCart.getProducts())
                .build();
    }
}
