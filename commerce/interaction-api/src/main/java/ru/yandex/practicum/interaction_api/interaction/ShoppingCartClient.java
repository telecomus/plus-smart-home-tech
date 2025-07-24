package ru.yandex.practicum.interaction_api.interaction;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

@FeignClient(name = "warehouse")
public interface ShoppingCartClient {
    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartDto getShoppingCart(String username);

    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addToShoppingCart(String username, Map<String, Long> products);

    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateShoppingCart(String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto removeFromShoppingCart(String username, List<String> products);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request);
}
