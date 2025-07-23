package ru.practicum.yandex.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.yandex.model.WarehouseProduct;
import ru.yandex.practicum.dto.NewProductInWarehouse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
	WarehouseProduct toWarehouse(NewProductInWarehouse request);
}
