package ru.yandex.practicum.telemetry.aggregator.service.equivalent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEquivalentChecker implements EquivalentChecker {
    @Override
    public boolean isEquivalent(Object object1, Object object2) {
        MotionSensorAvro obj1 = (MotionSensorAvro) object1;
        MotionSensorAvro obj2 = (MotionSensorAvro) object2;

        return obj1.getMotion() == obj2.getMotion() &&
                obj1.getLinkQuality() == obj2.getLinkQuality() &&
                obj1.getVoltage() == obj2.getVoltage();
    }

    @Override
    public Class<?> getCheckedClass() {
        return MotionSensorAvro.class;
    }
}
