package ru.yandex.practicum.telemetry.collector.rest.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;

@Component
public class ClimateSensorEventHandler extends SensorEventHandler<ClimateSensorEvent> {
    @Autowired
    public ClimateSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public Object getPayload(ClimateSensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .setTemperatureC(event.getTemperatureC())
                .build();
    }

    @Override
    public SensorEvent.SensorEventType getEventType() {
        return SensorEvent.SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
