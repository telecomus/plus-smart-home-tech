package ru.yandex.practicum.telemetry.collector.rest.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;

@Component
public class MotionSensorEventHandler extends SensorEventHandler<MotionSensorEvent> {
    @Autowired
    public MotionSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public Object getPayload(MotionSensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setMotion(event.getMotion())
                .setLinkQuality(event.getLinkQuality())
                .setVoltage(event.getVoltage())
                .build();
    }

    @Override
    public SensorEvent.SensorEventType getEventType() {
        return SensorEvent.SensorEventType.MOTION_SENSOR_EVENT;
    }
}
