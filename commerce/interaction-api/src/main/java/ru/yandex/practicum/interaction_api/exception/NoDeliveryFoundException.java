package ru.yandex.practicum.interaction_api.exception;

public class NoDeliveryFoundException extends BaseHttpException {
    public NoDeliveryFoundException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
