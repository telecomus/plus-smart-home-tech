package ru.yandex.practicum.commerce.order.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;

@Component
public class OrderMapper {
    public OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .products(order.getProducts())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }
}
