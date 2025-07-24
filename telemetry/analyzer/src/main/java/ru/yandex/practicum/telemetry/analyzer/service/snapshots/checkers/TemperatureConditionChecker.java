package ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class TemperatureConditionChecker extends AnalyzerConditionChecker {
    public boolean check(Condition condition, SensorStateAvro state) {
        return switch (state.getData()) {
            case TemperatureSensorAvro sensor ->
                    isOperationTrue(condition.getOperation(), condition.getValue(), sensor.getTemperatureC());
            case ClimateSensorAvro sensor ->
                    isOperationTrue(condition.getOperation(), condition.getValue(), sensor.getTemperatureC());
            default -> throw new IllegalStateException("Unexpected value: " + state.getData());
        };
    }

    public ConditionTypeAvro getCheckedType() {
        return ConditionTypeAvro.TEMPERATURE;
    }
}
