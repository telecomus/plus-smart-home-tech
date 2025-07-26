package ru.yandex.practicum.interaction_api.interaction;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.payment.PaymentDto;

@FeignClient(name = "payment")
public interface PaymentClient {
    @PostMapping("/api/v1/payment")
    PaymentDto createPayment(OrderDto orderDto) throws FeignException;

    @PostMapping("/api/v1/payment/totalCost")
    Double calculateTotalCoast(OrderDto orderDto) throws FeignException;

    @PostMapping("/api/v1/payment/refund")
    void refund(String paymentId) throws FeignException;

    @PostMapping("/api/v1/payment/productCost")
    Double calculateProductCoast(OrderDto orderDto) throws FeignException;

    @PostMapping("/api/v1/payment/failed")
    void failedPayment(String paymentId) throws FeignException;
}
