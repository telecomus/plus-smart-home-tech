package ru.yandex.practicum.interaction_api.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {
    @NotNull
    Double deliveryWeight;

    @NotNull
    Double deliveryVolume;

    @NotNull
    Boolean fragile;
}
