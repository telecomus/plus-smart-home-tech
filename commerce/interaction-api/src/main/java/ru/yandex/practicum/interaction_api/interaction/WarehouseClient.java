package ru.yandex.practicum.interaction_api.interaction;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.ShippedToDeliveryRequest;

import java.util.Map;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PutMapping("/api/v1/warehouse")
    void createProduct(NewProductInWarehouseRequest request) throws FeignException;

    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto) throws FeignException;

    @PostMapping("/api/v1/warehouse/shipped")
    void shipped(ShippedToDeliveryRequest request) throws FeignException;

    @PostMapping("/api/v1/warehouse/return")
    void returnProducts(Map<String, Long> products) throws FeignException;

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto assembly(AssemblyProductsForOrderRequest request) throws FeignException;

    @PostMapping("/api/v1/warehouse/add")
    void addProduct(AddProductToWarehouseRequest request) throws FeignException;

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress()  throws FeignException;
}
