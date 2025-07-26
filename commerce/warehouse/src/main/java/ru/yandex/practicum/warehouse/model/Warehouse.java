package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Builder
@Table(name = "warehouse")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
    @Id
    @Column(name = "product_id")
    String productId;

    @Column(name = "width", nullable = false)
    @Min(1)
    Double width;

    @Column(name = "height", nullable = false)
    @Min(1)
    Double height;

    @Column(name = "depth", nullable = false)
    @Min(1)
    Double depth;

    @Column(name = "weight", nullable = false)
    @Min(1)
    Double weight;

    @Column(name = "fragile", nullable = false)
    @Builder.Default
    Boolean fragile = false;

    @Column(name = "quantity", nullable = false)
    @Builder.Default
    Long quantity = 0L;
}
