package ru.yandex.practicum.telemetry.collector.rest.handlers.sensor;

import lombok.AllArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;

@ToString
@AllArgsConstructor
public abstract class SensorEventHandler<T extends SensorEvent> {
    private final KafkaClient kafkaClient;

    public void handle(T event) {
        Object eventPayload = getPayload(event);
        SensorEventAvro payload = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(eventPayload)
                .build();

        kafkaClient.sendData(event.getHubId(), payload, kafkaClient.getTelemetrySensorsV1Topic());
    }

    public abstract SensorEvent.SensorEventType getEventType();

    protected abstract Object getPayload(T event);
}
