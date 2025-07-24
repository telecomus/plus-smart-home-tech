package ru.yandex.practicum.telemetry.aggregator.service.equivalent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEquivalentChecker implements EquivalentChecker {
    @Override
    public boolean isEquivalent(Object object1, Object object2) {
        LightSensorAvro obj1 = (LightSensorAvro) object1;
        LightSensorAvro obj2 = (LightSensorAvro) object2;

        return obj1.getLuminosity() == obj2.getLuminosity() &&
                obj1.getLinkQuality() == obj2.getLinkQuality();
    }

    @Override
    public Class<?> getCheckedClass() {
        return LightSensorAvro.class;
    }
}
