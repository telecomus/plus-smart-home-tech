package ru.yandex.practicum.interaction_api.exception;

public class NoOrderFoundException extends BaseHttpException {
    public NoOrderFoundException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
