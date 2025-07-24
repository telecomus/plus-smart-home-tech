package ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class HumidityConditionChecker extends AnalyzerConditionChecker {
    public boolean check(Condition condition, SensorStateAvro state) {
        ClimateSensorAvro sensor = (ClimateSensorAvro) state.getData();
        return isOperationTrue(condition.getOperation(), condition.getValue(), sensor.getHumidity());
    }

    public ConditionTypeAvro getCheckedType() {
        return ConditionTypeAvro.HUMIDITY;
    }
}
