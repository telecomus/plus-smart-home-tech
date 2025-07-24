package ru.yandex.practicum.shopping_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction_api.dto.product.ProductCategory;
import ru.yandex.practicum.interaction_api.dto.product.ProductDto;
import ru.yandex.practicum.interaction_api.dto.product.ProductState;
import ru.yandex.practicum.interaction_api.dto.product.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction_api.exception.ProductNotFoundException;
import ru.yandex.practicum.shopping_store.mapper.ProductMapper;
import ru.yandex.practicum.shopping_store.model.Product;
import ru.yandex.practicum.shopping_store.storage.ShoppingStoreRepository;

@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreRepository repository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDto> getShoppingStore(ProductCategory category, Pageable pageable) {
        Page<Product> products = repository.findAllByProductCategory(category, pageable);
        return products.map(productMapper::toProductDto);
    }

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = repository.save(productMapper.fromProductDto(productDto));
        return productMapper.toProductDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product originProduct = getProductWithCheck(productDto.getProductId());
        Product updatedProduct = productMapper.fromProductDto(productDto);

        updatedProduct.setProductId(productDto.getProductId());

        if (updatedProduct.getImageSrc() == null) {
            updatedProduct.setImageSrc(originProduct.getImageSrc());
        }

        if (updatedProduct.getProductCategory() == null) {
            updatedProduct.setProductCategory(originProduct.getProductCategory());
        }

        return productMapper.toProductDto(repository.save(updatedProduct));
    }

    @Override
    public Boolean removeProductFromStore(String productId) {
        Product product = getProductWithCheck(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productMapper.toProductDto(repository.save(product));
        return true;
    }

    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProductWithCheck(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        productMapper.toProductDto(repository.save(product));
        return true;
    }

    @Override
    public ProductDto getProduct(String productId) {
        return productMapper.toProductDto(getProductWithCheck(productId));
    }

    private Product getProductWithCheck(String id) {
        return repository.findByProductId(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product %s not found.", id),
                        HttpStatus.NOT_FOUND.toString()
                ));
    }
}
