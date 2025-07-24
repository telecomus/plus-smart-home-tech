package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction_api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction_api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction_api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction_api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction_api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.Warehouse;
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
        Set<String> productIds = shoppingCartDto.getProducts().keySet();
        List<Warehouse> products = repository.findAllById(productIds);
        Map<String, Long> productsOnWarehouse = products.stream()
                .collect(Collectors.toMap(Warehouse::getProductId, Warehouse::getQuantity));

        for (String productId : shoppingCartDto.getProducts().keySet()) {
            Long quantity = productsOnWarehouse.getOrDefault(productId, 0L);

            if (quantity < shoppingCartDto.getProducts().get(productId)) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        String.format("Product %s not enough.", productId),
                        HttpStatus.BAD_REQUEST.toString()
                );
            }
        }

        return new BookedProductsDto();
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        Warehouse warehouse = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        String.format("There ere no product %s in warehouse.", request.getProductId()),
                        HttpStatus.BAD_REQUEST.toString()
                ));
        warehouse.setQuantity(warehouse.getQuantity() + request.getQuantity());
        repository.save(warehouse);
    }

    @Override
    public AddressDto getShoppingCart() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }
}
