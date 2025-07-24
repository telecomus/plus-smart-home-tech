package ru.yandex.practicum.interaction_api.exception;

public class ProductInShoppingCartLowQuantityInWarehouse extends BaseHttpException {
    public ProductInShoppingCartLowQuantityInWarehouse(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
