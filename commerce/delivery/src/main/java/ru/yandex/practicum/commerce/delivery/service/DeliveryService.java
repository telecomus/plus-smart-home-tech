package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(String deliveryId);

    void pickedDelivery(String deliveryId);

    void failedDelivery(String deliveryId);

    Double calculateCost(OrderDto orderDto);
}
