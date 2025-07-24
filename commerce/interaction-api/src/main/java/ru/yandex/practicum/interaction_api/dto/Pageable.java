package ru.yandex.practicum.interaction_api.dto;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pageable {
    @Min(0)
    Integer page;

    @Min(1)
    Integer size;

    List<String> sort;
}
