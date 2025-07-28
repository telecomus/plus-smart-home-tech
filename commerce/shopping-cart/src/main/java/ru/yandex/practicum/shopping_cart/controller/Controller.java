package ru.yandex.practicum.shopping_cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.interaction.ShoppingCartClient;
import ru.yandex.practicum.shopping_cart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}/shopping-cart")
@RequiredArgsConstructor
public class Controller implements ShoppingCartClient {
    private final ShoppingCartService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return service.getShoppingCart(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addToShoppingCart(@RequestParam String username, @RequestBody Map<String, Long> products) {
        return service.addToShoppingCart(username, products);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateShoppingCart(@RequestParam String username) {
        service.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeFromShoppingCart(@RequestParam String username, @RequestBody List<String> products) {
        return service.removeFromShoppingCart(username, products);
    }

    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeQuantity(@RequestParam String username,
                                          @Valid @RequestBody ChangeProductQuantityRequest request) {
        return service.changeQuantity(username, request);
    }

    @GetMapping("username")
    @ResponseStatus(HttpStatus.OK)
    public String getUserName(@RequestParam String shoppingCartId) {
        return service.getUserName(shoppingCartId);
    }
}
