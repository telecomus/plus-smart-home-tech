package ru.yandex.practicum.interaction_api.exception;

public class NoSpecifiedProductInWarehouseException extends BaseHttpException {
    public NoSpecifiedProductInWarehouseException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
