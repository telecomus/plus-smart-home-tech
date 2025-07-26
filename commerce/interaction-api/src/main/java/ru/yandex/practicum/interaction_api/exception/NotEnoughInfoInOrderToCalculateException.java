package ru.yandex.practicum.interaction_api.exception;

public class NotEnoughInfoInOrderToCalculateException extends BaseHttpException {
    public NotEnoughInfoInOrderToCalculateException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
