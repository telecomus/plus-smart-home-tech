package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.storage.OrderRepository;
import ru.yandex.practicum.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction_api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.interaction_api.dto.order.OrderState;
import ru.yandex.practicum.interaction_api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.exception.NoOrderFoundException;
import ru.yandex.practicum.interaction_api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.interaction_api.interaction.DeliveryClient;
import ru.yandex.practicum.interaction_api.interaction.PaymentClient;
import ru.yandex.practicum.interaction_api.interaction.ShoppingCartClient;
import ru.yandex.practicum.interaction_api.interaction.WarehouseClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderMapper orderMapper;

    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final ShoppingCartClient shoppingCartClient;

    @Override
    public List<OrderDto> getOrders(String username) {
        checkUsername(username);
        return repository.findAllByUsername(username).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        String shoppingCartId = request.getShoppingCart().getShoppingCartId();
        String username = shoppingCartClient.getUserName(shoppingCartId);
        AddressDto warehouseAddress = warehouseClient.getAddress();
        AddressDto clientAddress = request.getDeliveryAddress();

        Order order = repository.save(Order.builder()
                        .username(username)
                        .shoppingCartId(shoppingCartId)
                        .products(request.getShoppingCart().getProducts())
                .build());

        BookedProductsDto booked = warehouseClient
                .assembly(new AssemblyProductsForOrderRequest(order.getProducts(), order.getOrderId()));

        order.setFragile(booked.getFragile());
        order.setDeliveryWeight(booked.getDeliveryWeight());
        order.setDeliveryVolume(booked.getDeliveryVolume());

        DeliveryDto delivery = deliveryClient.createDelivery(DeliveryDto.builder()
                        .orderId(order.getOrderId())
                        .toAddress(clientAddress)
                        .fromAddress(warehouseAddress)
                .build());

        order.setDeliveryId(delivery.getDeliveryId());
        order = repository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        Order order = getOrderWithCheck(productReturnRequest.getOrderId());
        // обновить статус
        order.setState(OrderState.PRODUCT_RETURNED);
        // обновить остатки на складе
        warehouseClient.returnProducts(productReturnRequest.getProducts());
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto paymentOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.PAID);
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto failedPaymentOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.DELIVERED);
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto failedDeliveryOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto completedOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.COMPLETED);
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto calculateTotal(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);

        if (order.getTotalPrice() == null) {
            Double totalPrice = paymentClient.calculateTotalCoast(orderMapper.toOrderDto(order));
            order.setTotalPrice(totalPrice);
            order = repository.save(order);
        }

        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.ASSEMBLED);
        return orderMapper.toOrderDto(repository.save(order));
    }

    @Override
    public OrderDto failedAssemblyOrder(String orderId) {
        orderId = trimQuotes(orderId);
        Order order = getOrderWithCheck(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toOrderDto(repository.save(order));
    }

    private void checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Empty username.", HttpStatus.UNAUTHORIZED.toString());
        }
    }

    private Order getOrderWithCheck(String orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(
                        String.format("Order with id %s not found.", orderId),
                        HttpStatus.BAD_REQUEST.toString()
                ));
    }

    private String trimQuotes(String in) {
        return in.replaceAll("^\"|\"$", "");
    }
}
