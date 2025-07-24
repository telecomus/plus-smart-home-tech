package ru.yandex.practicum.warehouse.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction_api.exception.BaseHttpException;
import ru.yandex.practicum.interaction_api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction_api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction_api.exception.SpecifiedProductAlreadyInWarehouseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseHttpException handleProductInShoppingCartLowQuantityInWarehouse(final ProductInShoppingCartLowQuantityInWarehouse e) {
        log.warn("400 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseHttpException handleSpecifiedProductAlreadyInWarehouseException(final SpecifiedProductAlreadyInWarehouseException e) {
        log.warn("400 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseHttpException handleNoSpecifiedProductInWarehouseException(final NoSpecifiedProductInWarehouseException e) {
        log.warn("400 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseHttpException handleAnyException(final Exception e) {
        log.warn("500 {}", e.getMessage(), e);
        return new BaseHttpException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}