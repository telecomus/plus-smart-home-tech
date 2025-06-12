package ru.yandex.practicum.telemetry.collector.rest.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.TemperatureSensorEvent;

@Component
public class TemperatureSensorEventHandler extends SensorEventHandler<TemperatureSensorEvent> {
    @Autowired
    public TemperatureSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public Object getPayload(TemperatureSensorEvent event) {
        return TemperatureSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }

    @Override
    public SensorEvent.SensorEventType getEventType() {
        return SensorEvent.SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
