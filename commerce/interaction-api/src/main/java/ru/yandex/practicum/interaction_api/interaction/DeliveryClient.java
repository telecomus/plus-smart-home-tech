package ru.yandex.practicum.interaction_api.interaction;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;

@FeignClient(name = "delivery")
public interface DeliveryClient {
    @PutMapping("/api/v1/delivery")
    DeliveryDto createDelivery(DeliveryDto deliveryDto) throws FeignException;

    @PostMapping("/api/v1/delivery/successful")
    void successfulDelivery(String deliveryId) throws FeignException;

    @PostMapping("/api/v1/delivery/picked")
    void pickedDelivery(String deliveryId) throws FeignException;

    @PostMapping("/api/v1/delivery/failed")
    void failedDelivery(String deliveryId) throws FeignException;

    @PostMapping("/api/v1/delivery/cost")
    Double calculateCost(OrderDto orderDto) throws FeignException;
}
