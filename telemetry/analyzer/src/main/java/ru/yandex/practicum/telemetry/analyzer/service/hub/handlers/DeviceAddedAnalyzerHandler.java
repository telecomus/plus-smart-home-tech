package ru.yandex.practicum.telemetry.analyzer.service.hub.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedAnalyzerHandler implements AnalyzerHubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro hubEvent) {
        DeviceAddedEventAvro eventAvro = (DeviceAddedEventAvro) hubEvent.getPayload();

        Optional<Sensor> sensorOpt = sensorRepository.findByIdAndHubId(eventAvro.getId(), hubEvent.getHubId());

        if (sensorOpt.isPresent()) {
            log.warn("Устройство {} уже существует", sensorOpt.get());
            return;
        }

        Sensor sensor = Sensor.builder()
                .id(eventAvro.getId())
                .hubId(hubEvent.getHubId())
                .build();

        log.debug("Сохранение сенсора {}", sensor);
        sensorRepository.save(sensor);
        log.debug("Сенсор {} успешно сохранен", sensor);
    }

    @Override
    public Class<?> getCheckedClass() {
        return DeviceAddedEventAvro.class;
    }
}
