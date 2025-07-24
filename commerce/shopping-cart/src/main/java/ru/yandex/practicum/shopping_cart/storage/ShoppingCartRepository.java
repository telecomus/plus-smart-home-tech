package ru.yandex.practicum.shopping_cart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shopping_cart.model.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    Optional<ShoppingCart> findByOwner(String username);

    boolean existsByOwner(String username);
}
