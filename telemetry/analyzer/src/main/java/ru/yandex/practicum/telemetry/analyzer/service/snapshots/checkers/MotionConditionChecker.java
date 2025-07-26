package ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class MotionConditionChecker extends AnalyzerConditionChecker {
    public boolean check(Condition condition, SensorStateAvro state) {
        MotionSensorAvro sensor = (MotionSensorAvro) state.getData();
        return isOperationTrue(condition.getOperation(), condition.getValue(), fromBoolean(sensor.getMotion()));
    }

    public ConditionTypeAvro getCheckedType() {
        return ConditionTypeAvro.MOTION;
    }
}
