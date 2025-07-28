package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction_api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction_api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction_api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.interaction_api.interaction.DeliveryClient;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.ProductInWarehouse;
import ru.yandex.practicum.warehouse.storage.WarehouseRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private static final String[] ADDRESSES = new String[] {"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    private final WarehouseRepository repository;
    private final WarehouseMapper warehouseMapper;

    private final DeliveryClient deliveryClient;

    @Override
    public void createProduct(NewProductInWarehouseRequest request) {
        if (repository.existsByProductId(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    String.format("Product %s already exists.", request.getProductId()),
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        repository.save(warehouseMapper.fromNewProductInWarehouseRequest(request));
    }

    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto) {
        checkProductsQuantity(shoppingCartDto.getProducts());
        return new BookedProductsDto();
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        ProductInWarehouse productInWarehouse = getProductWithCheck(request.getProductId());
        productInWarehouse.setQuantity(productInWarehouse.getQuantity() + request.getQuantity());
        repository.save(productInWarehouse);
    }

    @Override
    public AddressDto getAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    @Override
    public void shipped(ShippedToDeliveryRequest request) {
        deliveryClient.pickedDelivery(request.getDeliveryId());
    }

    @Override
    public void returnProducts(Map<String, Long> products) {
        for (Map.Entry<String, Long> entry : products.entrySet()) {
            addProduct(new AddProductToWarehouseRequest(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        checkProductsQuantity(request.getProducts());
        BookedProductsDto result = new BookedProductsDto(0d, 0d, false);

        for (Map.Entry<String, Long> entry : request.getProducts().entrySet()) {
            ProductInWarehouse product = getProductWithCheck(entry.getKey());
            product.setQuantity(product.getQuantity() - entry.getValue());
            repository.save(product);
            Double volume = product.getWidth() * product.getHeight() * product.getDepth();

            result.setDeliveryVolume(result.getDeliveryVolume() + volume);
            result.setDeliveryWeight(result.getDeliveryWeight() + product.getWeight());
            result.setFragile(result.getFragile() || product.getFragile());
        }

        return result;
    }

    private void checkProductsQuantity(Map<String, Long> products) {
        Set<String> productIds = products.keySet();
        List<ProductInWarehouse> productsInWarehouse = repository.findAllById(productIds);
        Map<String, Long> productsOnWarehouse = productsInWarehouse.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, ProductInWarehouse::getQuantity));

        for (String productId : products.keySet()) {
            Long quantity = productsOnWarehouse.getOrDefault(productId, 0L);

            if (quantity < products.get(productId)) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        String.format("Product %s not enough.", productId),
                        HttpStatus.BAD_REQUEST.toString()
                );
            }
        }
    }

    private ProductInWarehouse getProductWithCheck(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        String.format("There ere no product %s in warehouse.", id),
                        HttpStatus.BAD_REQUEST.toString()
                ));
    }
}
