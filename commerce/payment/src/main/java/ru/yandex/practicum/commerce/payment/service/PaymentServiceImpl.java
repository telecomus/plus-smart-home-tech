package ru.yandex.practicum.commerce.payment.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.model.PaymentStatus;
import ru.yandex.practicum.commerce.payment.storage.PaymentRepository;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction_api.dto.product.ProductDto;
import ru.yandex.practicum.interaction_api.exception.NoOrderFoundException;
import ru.yandex.practicum.interaction_api.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.interaction_api.interaction.DeliveryClient;
import ru.yandex.practicum.interaction_api.interaction.OrderClient;
import ru.yandex.practicum.interaction_api.interaction.ShoppingStoreClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;

    private final PaymentRepository repository;

    private final ShoppingStoreClient shoppingStoreClient;
    private final DeliveryClient deliveryClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        double productsCoast = calculateProductCoast(orderDto);
        Payment payment = repository.save(Payment.builder()
                        .totalPayment(calculateTotalCoast(orderDto))
                        .deliveryPrice(deliveryClient.calculateCost(orderDto))
                        .feeTotal(productsCoast * 0.1)
                .build());

        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public Double calculateTotalCoast(OrderDto orderDto) {
        double productsCoast = calculateProductCoast(orderDto);
        // прибавить 10% НДС
        productsCoast *= 1.1;

        try {
            // прибавить стоимость доставки
            productsCoast += deliveryClient.calculateCost(orderDto);
        } catch (FeignException e) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Not enough info in order to calculate.",
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        return productsCoast;
    }

    @Override
    public void refund(String paymentId) {
        Payment payment = getPaymentWithCheck(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        orderClient.paymentOrder(payment.getOrderId());
        repository.save(payment);
    }

    @Override
    public Double calculateProductCoast(OrderDto orderDto) {
        double result = 0;

        try {
            for (Map.Entry<String, Long> entry : orderDto.getProducts().entrySet()) {
                ProductDto productDto = shoppingStoreClient.getProduct(entry.getKey());
                result += productDto.getPrice() * entry.getValue();
            }
        } catch (FeignException e) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Not enough info in order to calculate.",
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        return result;
    }

    @Override
    public void failedPayment(String paymentId) {
        Payment payment = getPaymentWithCheck(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        orderClient.failedPaymentOrder(payment.getOrderId());
        repository.save(payment);
    }

    private Payment getPaymentWithCheck(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoOrderFoundException(
                        String.format("Payment with id %s not found.", id),
                        HttpStatus.BAD_REQUEST.toString()
                ));
    }
}
