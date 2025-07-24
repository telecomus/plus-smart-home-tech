package ru.yandex.practicum.shopping_store.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.interaction_api.dto.product.ProductCategory;
import ru.yandex.practicum.shopping_store.model.Product;

import java.util.Optional;

public interface ShoppingStoreRepository extends JpaRepository<Product, String> {
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);

    Optional<Product> findByProductId(String productId);
}
