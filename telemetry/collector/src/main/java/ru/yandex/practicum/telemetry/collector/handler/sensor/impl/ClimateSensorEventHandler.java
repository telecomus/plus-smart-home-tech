package ru.yandex.practicum.telemetry.collector.handler.sensor.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.telemetry.collector.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

@Component
@RequiredArgsConstructor
public class ClimateSensorEventHandler implements SensorEventHandler<ClimateSensorEvent> {
    private final KafkaEventProducer producer;
    private static final String TOPIC = "telemetry.sensors.v1";

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(ClimateSensorEvent event) {
        var avro = SensorEventMapper.toAvro(event);
        producer.send(TOPIC, event.getId(), avro);
    }
}
