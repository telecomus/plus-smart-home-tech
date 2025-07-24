package ru.yandex.practicum.telemetry.analyzer.service.hub.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Component
@RequiredArgsConstructor
public class DeviceRemovedAnalyzerHandler implements AnalyzerHubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro hubEvent) {
        DeviceRemovedEventAvro eventAvro = (DeviceRemovedEventAvro) hubEvent.getPayload();
        sensorRepository.deleteById(eventAvro.getId());
    }

    @Override
    public Class<?> getCheckedClass() {
        return DeviceRemovedEventAvro.class;
    }
}
