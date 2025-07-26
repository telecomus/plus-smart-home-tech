package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.order.service.OrderService;
import ru.yandex.practicum.interaction_api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction_api.interaction.OrderClient;

import java.util.List;

@RestController
@RequestMapping("${api.path}/order")
@RequiredArgsConstructor
public class Controller implements OrderClient {
    private final OrderService service;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrders(@RequestParam String username) {
        return service.getOrders(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createOrder(@Validated @RequestBody CreateNewOrderRequest request) {
        return service.createOrder(request);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return service.returnOrder(productReturnRequest);
    }

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentOrder(String orderId) {
        return service.paymentOrder(orderId);
    }

    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto failedPaymentOrder(String orderId) {
        return service.failedPaymentOrder(orderId);
    }

    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto deliveryOrder(String orderId) {
        return service.deliveryOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto failedDeliveryOrder(String orderId) {
        return service.failedDeliveryOrder(orderId);
    }

    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto completedOrder(String orderId) {
        return service.completedOrder(orderId);
    }

    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateTotal(String orderId) {
        return service.calculateTotal(orderId);
    }

    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyOrder(String orderId) {
        return service.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto failedAssemblyOrder(String orderId) {
        return service.failedAssemblyOrder(orderId);
    }
}
