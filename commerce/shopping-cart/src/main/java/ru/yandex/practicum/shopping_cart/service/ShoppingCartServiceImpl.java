package ru.yandex.practicum.shopping_cart.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction_api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.interaction_api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction_api.interaction.WarehouseClient;
import ru.yandex.practicum.shopping_cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping_cart.model.ShoppingCart;
import ru.yandex.practicum.shopping_cart.storage.ShoppingCartRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addToShoppingCart(String username, Map<String, Long> products) {
        checkUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);

        if (!shoppingCart.getIsActive()) {
            return shoppingCartMapper.toShoppingCartDto(shoppingCart);
        }

        Map<String, Long> cartProducts = shoppingCart.getProducts();

        for (Map.Entry<String, Long> entry : products.entrySet()) {
            String product = entry.getKey();
            Long current = cartProducts.getOrDefault(product, 0L);
            cartProducts.put(product, current + entry.getValue());
        }

        checkQuantityAvailable(shoppingCart);
        return shoppingCartMapper.toShoppingCartDto(repository.save(shoppingCart));
    }

    @Override
    public void deactivateShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);
        shoppingCart.setIsActive(false);
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<String> products) {
        checkUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);

        if (!shoppingCart.getIsActive()) {
            return shoppingCartMapper.toShoppingCartDto(shoppingCart);
        }

        Map<String, Long> cartProducts = shoppingCart.getProducts();

        for (String product : products) {
            if (!cartProducts.containsKey(product)) {
                throw new NoProductsInShoppingCartException(
                        product + "not in shopping cart.",
                        HttpStatus.BAD_REQUEST.toString()
                );
            }

            cartProducts.remove(product);
        }

        return shoppingCartMapper.toShoppingCartDto(repository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        checkUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);

        if (!shoppingCart.getIsActive()) {
            return shoppingCartMapper.toShoppingCartDto(shoppingCart);
        }

        Map<String, Long> cartProducts = shoppingCart.getProducts();
        String product = request.getProductId();

        if (!cartProducts.containsKey(product)) {
            throw new NoProductsInShoppingCartException(
                    product + " not in shopping cart.",
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        cartProducts.put(product, request.getNewQuantity());
        return shoppingCartMapper.toShoppingCartDto(repository.save(shoppingCart));
    }

    @Override
    public String getUserName(String shoppingCartId) {
        ShoppingCart shoppingCart = repository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("NShopping cart not found.")
        );

        return shoppingCart.getOwner();
    }

    private void checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Empty username.", HttpStatus.UNAUTHORIZED.toString());
        }
    }

    private ShoppingCart getOrCreateShoppingCart(String username) {
        return repository.findByOwner(username)
                .orElseGet(() -> repository.save(ShoppingCart.builder()
                                .owner(username)
                                .build())
                );
    }

    private void  checkQuantityAvailable(ShoppingCart shoppingCart) {
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(shoppingCart);

        try {
            warehouseClient.checkQuantity(shoppingCartDto);
        } catch (FeignException e) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    "Product not enough.",
                    HttpStatus.BAD_REQUEST.toString()
            );
        }
    }
}
