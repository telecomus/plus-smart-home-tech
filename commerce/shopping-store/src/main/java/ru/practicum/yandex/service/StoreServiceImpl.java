package ru.practicum.yandex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.mapper.ProductMapper;
import ru.practicum.yandex.model.Product;
import ru.practicum.yandex.repository.StoreRepository;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductCountState;
import ru.yandex.practicum.exeption.ConditionsNotMetException;
import ru.yandex.practicum.exeption.NotFoundException;
import ru.yandex.practicum.types.ProductCategory;
import ru.yandex.practicum.types.ProductState;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
	private final StoreRepository storeRepository;
	private final ProductMapper productMapper;

	@Override
	public List<ProductDto> getProductsByCategory(ProductCategory category, ru.yandex.practicum.dto.Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(),
				Sort.by(Sort.DEFAULT_DIRECTION, String.join(",", pageable.getSort())));
		List<Product> products = storeRepository.findAllByProductCategory(category, pageRequest);

		return productMapper.mapListProducts(products);
	}

	@Transactional
	@Override
	public ProductDto createProduct(ProductDto productDto) {
		if (storeRepository.getByProductId(productDto.getProductId()).isPresent())
			throw new ConditionsNotMetException("Создаваемый товар уже есть в базе данных");
		Product product = productMapper.productDtoToProduct(productDto);

		return productMapper.productToProductDto(storeRepository.save(product));
	}

	@Transactional
	@Override
	public ProductDto updateProduct(ProductDto productDto) {
		getProduct(productDto.getProductId());

		return productMapper.productToProductDto(
				storeRepository.save(productMapper.productDtoToProduct(productDto)));
	}

	@Transactional
	@Override
	public boolean removeProduct(String productId) {
		Product product = getProduct(productId);
		product.setProductState(ProductState.DEACTIVATE);
		storeRepository.save(product);

		return true;
	}

	@Transactional
	@Override
	public boolean changeState(SetProductCountState request) {
		Product product = getProduct(request.getProductId());
		product.setQuantityState(request.getQuantityState());
		storeRepository.save(product);

		return true;
	}

	@Override
	public ProductDto getInfoByProduct(String productId) {
		return productMapper.productToProductDto(getProduct(productId));
	}

	private Product getProduct(String idProduct) {
		Optional<Product> product = storeRepository.getByProductId(idProduct);
		if (product.isEmpty())
			throw new NotFoundException("Не найден продукт с id " + idProduct);

		return product.get();
	}
}
