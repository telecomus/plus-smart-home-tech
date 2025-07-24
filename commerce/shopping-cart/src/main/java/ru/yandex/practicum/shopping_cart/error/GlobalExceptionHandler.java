package ru.yandex.practicum.shopping_cart.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction_api.exception.BaseHttpException;
import ru.yandex.practicum.interaction_api.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction_api.exception.NotAuthorizedUserException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseHttpException handleNoProductsInShoppingCartException(final NoProductsInShoppingCartException e) {
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