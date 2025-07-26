package ru.yandex.practicum.interaction_api.exception;

public class SpecifiedProductAlreadyInWarehouseException extends BaseHttpException {
    public SpecifiedProductAlreadyInWarehouseException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
