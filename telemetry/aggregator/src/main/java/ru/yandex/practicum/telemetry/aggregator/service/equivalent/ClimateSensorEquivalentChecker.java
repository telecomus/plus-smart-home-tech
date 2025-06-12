package ru.yandex.practicum.telemetry.aggregator.service.equivalent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEquivalentChecker implements EquivalentChecker {
    @Override
    public boolean isEquivalent(Object object1, Object object2) {
        ClimateSensorAvro obj1 = (ClimateSensorAvro) object1;
        ClimateSensorAvro obj2 = (ClimateSensorAvro) object2;

        return obj1.getHumidity() == obj2.getHumidity() &&
                obj1.getCo2Level() == obj2.getCo2Level() &&
                obj1.getTemperatureC() == obj2.getTemperatureC();
    }

    @Override
    public Class<?> getCheckedClass() {
        return ClimateSensorAvro.class;
    }
}
