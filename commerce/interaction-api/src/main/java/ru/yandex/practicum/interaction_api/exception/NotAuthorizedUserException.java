package ru.yandex.practicum.interaction_api.exception;

public class NotAuthorizedUserException extends BaseHttpException {
    public NotAuthorizedUserException(String message, String httpStatus) {
        super(message, httpStatus);
    }
}
