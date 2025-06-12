package ru.yandex.practicum.telemetry.aggregator.service.equivalent;

public interface EquivalentChecker {
    boolean isEquivalent(Object object1, Object object2);

    Class<?> getCheckedClass();
}
