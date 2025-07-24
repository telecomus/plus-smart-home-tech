package ru.yandex.practicum.telemetry.aggregator.service.equivalent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEquivalentChecker implements EquivalentChecker {
    @Override
    public boolean isEquivalent(Object object1, Object object2) {
        TemperatureSensorAvro obj1 = (TemperatureSensorAvro) object1;
        TemperatureSensorAvro obj2 = (TemperatureSensorAvro) object2;

        return obj1.getTemperatureC() == obj2.getTemperatureC() &&
                obj1.getTemperatureF() == obj2.getTemperatureF();
    }

    @Override
    public Class<?> getCheckedClass() {
        return TemperatureSensorAvro.class;
    }
}
