package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryState;

@Getter
@Setter
@Entity
@Builder
@Table(name = "delivery")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    String deliveryId;

    @Column(name = "from_address_id", nullable = false)
    Address fromAddress;

    @Column(name = "to_address_id", nullable = false)
    Address toAddress;

    @Column(name = "order_id", nullable = false)
    String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false)
    @Builder.Default
    DeliveryState deliveryState = DeliveryState.CREATED;
}
