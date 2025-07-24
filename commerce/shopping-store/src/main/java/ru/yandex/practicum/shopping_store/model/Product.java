package ru.yandex.practicum.shopping_store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction_api.dto.product.ProductCategory;
import ru.yandex.practicum.interaction_api.dto.product.ProductState;
import ru.yandex.practicum.interaction_api.dto.product.QuantityState;

@Getter
@Setter
@Entity
@Builder
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    String productId;

    @Column(name = "product_name", nullable = false)
    @Size(min = 1)
    String productName;

    @Column(name = "description", nullable = false)
    @Size(min = 1)
    String description;

    @Column(name = "image_src", nullable = false)
    String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false)
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false)
    ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    ProductCategory productCategory;

    @Column(name = "price", nullable = false)
    @Min(1)
    Double price;
}
