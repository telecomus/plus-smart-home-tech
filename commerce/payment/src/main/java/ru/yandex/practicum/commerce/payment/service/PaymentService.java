package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.payment.PaymentDto;

public interface PaymentService {
    PaymentDto createPayment(OrderDto orderDto);

    Double calculateTotalCoast(OrderDto orderDto);

    void refund(String paymentId);

    Double calculateProductCoast(OrderDto orderDto);

    void failedPayment(String paymentId);
}
