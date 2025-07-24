package ru.yandex.practicum.interaction_api.interaction;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PutMapping("/api/v1/warehouse")
    void createProduct(NewProductInWarehouseRequest request);

    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto);

    @PostMapping("/api/v1/warehouse/add")
    void addProduct(AddProductToWarehouseRequest request);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getShoppingCart();
}
