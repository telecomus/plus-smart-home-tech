package ru.practicum.yandex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.model.Size;
import ru.practicum.yandex.model.WarehouseProduct;
import ru.practicum.yandex.repository.WarehouseRepository;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exeption.ConditionsNotMetException;
import ru.yandex.practicum.exeption.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
	private final WarehouseRepository warehouseRepository;

	@Transactional
	@Override
	public void createProductInWarehouse(NewProductInWarehouse request) {
		Optional<WarehouseProduct> product = findProduct(request.getProductId());
		if (product.isPresent()) {
			throw new ConditionsNotMetException("Невозможно добавить товар, который уже есть в базе данных");
		}

		WarehouseProduct newProduct = WarehouseProduct.builder()
				.productId(request.getProductId()) // ← обязательно!
				.quantity(1)
				.fragile(request.isFragile())
				.weight(request.getWeight())
				.dimension(new Size(
						request.getDimension().getWidth(),
						request.getDimension().getHeight(),
						request.getDimension().getDepth()
				))
				.build();

		warehouseRepository.save(newProduct);
	}

	@Override
	public ReserveProductsDto checkCountProducts(CartDto cartDto) {
		Map<String, Long> products = cartDto.getProducts();
		List<WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(products.keySet());
		warehouseProducts.forEach(warehouseProduct -> {
			if (warehouseProduct.getQuantity() < products.get(warehouseProduct.getProductId()))
				throw new ConditionsNotMetException("Не достаточное количество товаров на складе");
		});

		double deliveryWeight = warehouseProducts.stream()
				.map(WarehouseProduct::getWeight)
				.mapToDouble(Double::doubleValue)
				.sum();

		double deliveryVolume = warehouseProducts.stream()
				.map(warehouseProduct -> warehouseProduct.getDimension().getDepth()
						* warehouseProduct.getDimension().getHeight() * warehouseProduct.getDimension().getWidth())
				.mapToDouble(Double::doubleValue)
				.sum();

		boolean fragile = warehouseProducts.stream()
				.anyMatch(WarehouseProduct::isFragile);

		return ReserveProductsDto.builder()
				.deliveryWeight(deliveryWeight)
				.deliveryVolume(deliveryVolume)
				.fragile(fragile)
				.build();
	}

	@Transactional
	@Override
	public void addProductInWarehouse(AddProductInWarehouse request) {
		Optional<WarehouseProduct> product = findProduct(request.getProductId());
		if (product.isEmpty()) {
			throw new NotFoundException("Отсутствует данный товар");
		}
		WarehouseProduct pr = product.get();
		pr.setQuantity(pr.getQuantity() + request.getQuantity());
		warehouseRepository.save(pr);
	}

	@Override
	public AddressWarehouseDto getAddressWarehouse() {
		return AddressWarehouseDto.builder()
				.country("Россия")
				.city("Воронеж")
				.street("Лизюкова")
				.house("1")
				.flat("5")
				.build();
	}

	private Optional<WarehouseProduct> findProduct(String productId) {
		return warehouseRepository.findById(productId);
	}
}
