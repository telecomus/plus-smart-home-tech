package ru.yandex.practicum.telemetry.aggregator.service.equivalent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEquivalentChecker implements EquivalentChecker {
    @Override
    public boolean isEquivalent(Object object1, Object object2) {
        SwitchSensorAvro obj1 = (SwitchSensorAvro) object1;
        SwitchSensorAvro obj2 = (SwitchSensorAvro) object2;

        return obj1.getState() == obj2.getState();
    }

    @Override
    public Class<?> getCheckedClass() {
        return SwitchSensorAvro.class;
    }
}
