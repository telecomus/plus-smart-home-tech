package ru.yandex.practicum.interaction_api.interaction;

import feign.FeignException;
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
    ShoppingCartDto getShoppingCart(String username) throws FeignException;

    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addToShoppingCart(String username, Map<String, Long> products) throws FeignException;

    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateShoppingCart(String username) throws FeignException;

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto removeFromShoppingCart(String username, List<String> products) throws FeignException;

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) throws FeignException;

    @GetMapping("/api/v1/shopping-cart/username")
    String getUserName(String shoppingCartId) throws FeignException;
}
