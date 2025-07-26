package ru.yandex.practicum.warehouse.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.model.ProductInWarehouse;

@Component
public class WarehouseMapper {
    public ProductInWarehouse fromNewProductInWarehouseRequest(NewProductInWarehouseRequest request) {
        return ProductInWarehouse.builder()
                .productId(request.getProductId())
                .weight(request.getWeight())
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .fragile(request.getFragile())
                .build();
    }
}
