package ru.yandex.practicum.interaction_api.exception;

public class NoProductsInShoppingCartException extends BaseHttpException {
    public NoProductsInShoppingCartException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
