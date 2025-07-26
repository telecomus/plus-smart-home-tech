package ru.yandex.practicum.interaction_api.exception;

public class ProductNotFoundException extends BaseHttpException {
  public ProductNotFoundException(String message, String httpStatus) {
    super(message, httpStatus);
  }
}