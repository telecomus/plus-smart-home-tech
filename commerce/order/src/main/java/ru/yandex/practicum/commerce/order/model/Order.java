package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction_api.dto.order.OrderState;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    String orderId;

    @Column(name = "username")
    String username;

    @Column(name = "shopping_cart_id")
    String shoppingCartId;

    @Column(name = "payment_id")
    String paymentId;

    @Column(name = "delivery_id")
    String deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    @Builder.Default
    OrderState state = OrderState.NEW;

    @Column(name = "delivery_weight")
    Double deliveryWeight;

    @Column(name = "delivery_volume")
    Double deliveryVolume;

    @Column(name = "fragile")
    Boolean fragile;

    @Column(name = "total_price")
    Double totalPrice;

    @Column(name = "delivery_price")
    Double deliveryPrice;

    @Column(name = "product_price")
    Double productPrice;

    @ElementCollection
    @CollectionTable(name = "orders_products",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "order_id")})
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Builder.Default
    private Map<String, Long> products = new HashMap<>();
}
