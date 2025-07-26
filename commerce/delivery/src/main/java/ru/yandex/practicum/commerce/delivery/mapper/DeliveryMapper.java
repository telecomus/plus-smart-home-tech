package ru.yandex.practicum.commerce.delivery.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.delivery.model.Address;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;

@Component
public class DeliveryMapper {
    public Delivery fromDeliveryDto(DeliveryDto deliveryDto, Address from, Address to) {
        return Delivery.builder()
                .fromAddress(from)
                .toAddress(to)
                .orderId(deliveryDto.getOrderId())
                .build();
    }

    public DeliveryDto toDelDeliveryDto(Delivery delivery, AddressDto from, AddressDto to) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryState(delivery.getDeliveryState())
                .fromAddress(from)
                .toAddress(to)
                .orderId(delivery.getOrderId())
                .build();
    }

    public Address fromAddressDto(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }

    public AddressDto toAddressDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }
}
