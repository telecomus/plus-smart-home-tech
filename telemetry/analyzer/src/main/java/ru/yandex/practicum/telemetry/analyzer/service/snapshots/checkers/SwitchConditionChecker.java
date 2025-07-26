package ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class SwitchConditionChecker extends AnalyzerConditionChecker {
    public boolean check(Condition condition, SensorStateAvro state) {
        SwitchSensorAvro sensor = (SwitchSensorAvro) state.getData();
        return isOperationTrue(condition.getOperation(), condition.getValue(), fromBoolean(sensor.getState()));
    }

    public ConditionTypeAvro getCheckedType() {
        return ConditionTypeAvro.SWITCH;
    }
}
