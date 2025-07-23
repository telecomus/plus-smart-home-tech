package ru.practicum.yandex.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.yandex.model.ShoppingCart;
import ru.yandex.practicum.dto.CartDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {
	CartDto toShoppingCartDto(ShoppingCart shoppingCart);
}