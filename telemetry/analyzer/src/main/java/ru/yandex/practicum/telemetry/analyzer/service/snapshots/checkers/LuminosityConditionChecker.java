package ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class LuminosityConditionChecker extends AnalyzerConditionChecker {
    public boolean check(Condition condition, SensorStateAvro state) {
        LightSensorAvro sensor = (LightSensorAvro) state.getData();
        return isOperationTrue(condition.getOperation(), condition.getValue(), sensor.getLuminosity());
    }

    public ConditionTypeAvro getCheckedType() {
        return ConditionTypeAvro.LUMINOSITY;
    }
}
