package ru.yandex.practicum.shopping_cart.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@Table(name = "shopping_carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shopping_cart_id")
    String shoppingCartId;

    @Column(name = "owner", nullable = false)
    @Size(min = 1)
    String owner;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_products",
            joinColumns = {@JoinColumn(name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")})
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Builder.Default
    private Map<String, Long> products = new HashMap<>();

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true;
}
