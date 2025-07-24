package ru.yandex.practicum.shopping_store.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction_api.exception.BaseHttpException;
import ru.yandex.practicum.interaction_api.exception.ProductNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseHttpException handleProductNotFoundException(final ProductNotFoundException e) {
        log.warn("404 {}", e.getMessage(), e);
        return e;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseHttpException handleAnyException(final Exception e) {
        log.warn("500 {}", e.getMessage(), e);
        return new BaseHttpException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}