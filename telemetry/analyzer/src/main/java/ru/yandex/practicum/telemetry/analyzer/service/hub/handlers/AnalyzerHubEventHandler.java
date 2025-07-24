package ru.yandex.practicum.telemetry.analyzer.service.hub.handlers;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface AnalyzerHubEventHandler {
    void handle(HubEventAvro hubEvent);

    Class<?> getCheckedClass();
}
