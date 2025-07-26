package ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Slf4j
public abstract class AnalyzerConditionChecker {
    public abstract boolean check(Condition condition, SensorStateAvro state);

    public abstract ConditionTypeAvro getCheckedType();

    protected boolean isOperationTrue(ConditionOperationAvro operation, Integer expected, Integer actual) {
        switch (operation) {
            case EQUALS -> {
                return expected.equals(actual);
            }

            case LOWER_THAN -> {
                return actual.compareTo(expected) < 0;
            }

            case GREATER_THAN -> {
                return actual.compareTo(expected) > 0;
            }
        }

        log.error("Неподдерживаемый тип операции {}", operation);
        return false;
    }

    protected Integer fromBoolean(Boolean b) {
        return b ? 1 : 0;
    }
}
