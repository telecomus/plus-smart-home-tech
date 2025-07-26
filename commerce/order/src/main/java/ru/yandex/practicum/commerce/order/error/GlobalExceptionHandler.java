package ru.yandex.practicum.commerce.order.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction_api.exception.BaseHttpException;
import ru.yandex.practicum.interaction_api.exception.NoOrderFoundException;
import ru.yandex.practicum.interaction_api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.interaction_api.exception.ProductInShoppingCartLowQuantityInWarehouse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseHttpException handleNoOrderFoundException(final NoOrderFoundException e) {
        log.warn("400 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseHttpException handleProductInShoppingCartLowQuantityInWarehouse(final ProductInShoppingCartLowQuantityInWarehouse e) {
        log.warn("400 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseHttpException handleNotAuthorizedUserException(final NotAuthorizedUserException e) {
        log.warn("401 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseHttpException handleAnyException(final Exception e) {
        log.warn("500 {}", e.getMessage(), e);
        return new BaseHttpException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}