package ru.practicum.yandex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.service.StoreService;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductCountState;
import ru.yandex.practicum.types.ProductCategory;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class StoreController {
	private final StoreService storeService;

	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, Pageable pageable) {
		log.info("Запрос на получения списка товаров по категории {} и страницам {}", category, pageable);
		return storeService.getProductsByCategory(category, pageable);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping
	public ProductDto createProduct(@RequestBody ProductDto productDto) {
		log.info("Запрос на создание нового товара {}", productDto);
		return storeService.createProduct(productDto);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping
	public ProductDto updateProduct(@RequestBody ProductDto productDto) {
		log.info("Запрос на обновление товара {}", productDto);
		return storeService.updateProduct(productDto);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/removeProductFromStore")
	public boolean removeProduct(@RequestParam String productId) {
		log.info("Запрос на удаление товара {}", productId);
		return storeService.removeProduct(productId);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/quantityState")
	public boolean changeState(SetProductCountState request) {
		log.info("Запрос на установку статуса для товара {}", request);
		return storeService.changeState(request);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{productId}")
	public ProductDto getInfoByProduct(@PathVariable String productId) {
		log.info("Запрос на получение информации о товаре {}", productId);
		return storeService.getInfoByProduct(productId);
	}
}
