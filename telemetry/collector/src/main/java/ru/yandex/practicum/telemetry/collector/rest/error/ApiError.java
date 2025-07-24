package ru.yandex.practicum.telemetry.collector.rest.error;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final String stackTrace;
}