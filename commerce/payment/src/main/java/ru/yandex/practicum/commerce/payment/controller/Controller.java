package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.payment.service.PaymentService;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction_api.interaction.PaymentClient;

@RestController
@RequestMapping("${api.path}/payment")
@RequiredArgsConstructor
public class Controller implements PaymentClient {
    private final PaymentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto createPayment(OrderDto orderDto) {
        return service.createPayment(orderDto);
    }

    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public Double calculateTotalCoast(OrderDto orderDto) {
        return service.calculateTotalCoast(orderDto);
    }

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void refund(String paymentId) {
        service.refund(paymentId);
    }

    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public Double calculateProductCoast(OrderDto orderDto) {
        return service.calculateProductCoast(orderDto);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void failedPayment(String paymentId) {
        service.failedPayment(paymentId);
    }
}
