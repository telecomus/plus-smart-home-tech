package ru.yandex.practicum.commerce.order.service;

import ru.yandex.practicum.interaction_api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.order.ProductReturnRequest;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrders(String username);

    OrderDto createOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    OrderDto paymentOrder(String orderId);

    OrderDto failedPaymentOrder(String orderId);

    OrderDto deliveryOrder(String orderId);

    OrderDto failedDeliveryOrder(String orderId);

    OrderDto completedOrder(String orderId);

    OrderDto calculateTotal(String orderId);

    OrderDto assemblyOrder(String orderId);

    OrderDto failedAssemblyOrder(String orderId);
}
