package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.Address;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.storage.AddressRepository;
import ru.yandex.practicum.commerce.delivery.storage.DeliveryRepository;
import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction_api.exception.NoDeliveryFoundException;
import ru.yandex.practicum.interaction_api.interaction.OrderClient;
import ru.yandex.practicum.interaction_api.interaction.WarehouseClient;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    @Value("${delivery.basePrice}")
    private Double basePrice;

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;

    private final DeliveryMapper deliveryMapper;

    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Address from = addressRepository.save(deliveryMapper.fromAddressDto(deliveryDto.getFromAddress()));
        Address to = addressRepository.save(deliveryMapper.fromAddressDto(deliveryDto.getToAddress()));
        Delivery delivery = deliveryRepository.save(deliveryMapper.fromDeliveryDto(deliveryDto, from, to));
        return deliveryMapper.toDelDeliveryDto(
                delivery,
                deliveryMapper.toAddressDto(from),
                deliveryMapper.toAddressDto(to)
        );
    }

    @Override
    public void successfulDelivery(String deliveryId) {
        deliveryId = trimQuotes(deliveryId);
        Delivery delivery = getDeliveryWithCheck(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.deliveryOrder(delivery.getOrderId());
        deliveryRepository.save(delivery);
    }

    @Override
    public void pickedDelivery(String deliveryId) {
        deliveryId = trimQuotes(deliveryId);
        Delivery delivery = getDeliveryWithCheck(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        warehouseClient.shipped(new ShippedToDeliveryRequest(delivery.getOrderId(), deliveryId));
        orderClient.assemblyOrder(delivery.getOrderId());
        deliveryRepository.save(delivery);
    }

    @Override
    public void failedDelivery(String deliveryId) {
        deliveryId = trimQuotes(deliveryId);
        Delivery delivery = getDeliveryWithCheck(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.failedDeliveryOrder(delivery.getOrderId());
        deliveryRepository.save(delivery);
    }

    @Override
    public Double calculateCost(OrderDto orderDto) {
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException(
                        String.format("Delivery for order id %s not found.", orderDto.getOrderId()),
                        HttpStatus.BAD_REQUEST.toString()
                ));
        double result = basePrice;

        if (delivery.getFromAddress().getCountry().equals("ADDRESS_1")) {
            result += basePrice;
        } else if ((delivery.getFromAddress().getCountry().equals("ADDRESS_2"))) {
            result += basePrice * 2;
        }

        if (orderDto.getFragile()) {
            result += result * 0.2;
        }

        result += orderDto.getDeliveryWeight() * 0.3;
        result += orderDto.getDeliveryVolume() * 0.2;

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            result += result * 0.2;
        }

        return result;
    }

    private Delivery getDeliveryWithCheck(String orderId) {
        return deliveryRepository.findById(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        String.format("Delivery with id %s not found.", orderId),
                        HttpStatus.BAD_REQUEST.toString()
                ));
    }

    private String trimQuotes(String in) {
        return in.replaceAll("^\"|\"$", "");
    }
}
