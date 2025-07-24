package ru.yandex.practicum.telemetry.collector.rest.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;

@Component
public class LightSensorEventHandler extends SensorEventHandler<LightSensorEvent> {
    @Autowired
    public LightSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public Object getPayload(LightSensorEvent event) {
        return LightSensorAvro.newBuilder()
                .setLuminosity(event.getLuminosity())
                .setLinkQuality(event.getLinkQuality())
                .build();
    }

    @Override
    public SensorEvent.SensorEventType getEventType() {
        return SensorEvent.SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
