package ru.yandex.practicum.telemetry.collector.rest.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SwitchSensorEvent;

@Component
public class SwitchSensorEventHandler extends SensorEventHandler<SwitchSensorEvent> {
    @Autowired
    public SwitchSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public Object getPayload(SwitchSensorEvent event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    @Override
    public SensorEvent.SensorEventType getEventType() {
        return SensorEvent.SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
