package ru.yandex.practicum.interaction_api.interaction;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction_api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.order.ProductReturnRequest;

import java.util.List;

@FeignClient(name = "order")
public interface OrderClient {
    @GetMapping("/api/v1/order")
    List<OrderDto> getOrders(String username) throws FeignException;

    @PutMapping("/api/v1/order")
    OrderDto createOrder(CreateNewOrderRequest request) throws FeignException;

    @PostMapping("/api/v1/order/return")
    OrderDto returnOrder(ProductReturnRequest productReturnRequest) throws FeignException;

    @PostMapping("/api/v1/order/payment")
    OrderDto paymentOrder(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/payment/failed")
    OrderDto failedPaymentOrder(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/delivery")
    OrderDto deliveryOrder(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto failedDeliveryOrder(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/completed")
    OrderDto completedOrder(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/calculate/total")
    OrderDto calculateTotal(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/assembly")
    OrderDto assemblyOrder(String orderId) throws FeignException;

    @PostMapping("/api/v1/order/assembly/failed")
    OrderDto failedAssemblyOrder(String orderId) throws FeignException;
}
