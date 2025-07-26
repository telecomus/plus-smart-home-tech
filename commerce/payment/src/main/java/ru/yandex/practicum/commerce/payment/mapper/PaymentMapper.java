package ru.yandex.practicum.commerce.payment.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.interaction_api.dto.payment.PaymentDto;

@Component
public class PaymentMapper {
    public PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryPrice())
                .feeTotal(payment.getFeeTotal())
                .build();
    }
}
