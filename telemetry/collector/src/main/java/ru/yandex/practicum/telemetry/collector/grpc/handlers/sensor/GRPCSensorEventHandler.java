package ru.yandex.practicum.telemetry.collector.grpc.handlers.sensor;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

import java.time.Instant;

@AllArgsConstructor
public abstract class GRPCSensorEventHandler {
    private final KafkaClient kafkaClient;

    public abstract SensorEventProto.PayloadCase getMessageType();

    protected abstract Object getPayload(SensorEventProto event);

    public void handle(SensorEventProto event) {
        Object eventPayload = getPayload(event);
        SensorEventAvro payload = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(eventPayload)
                .build();

        kafkaClient.sendData(event.getHubId(), payload, kafkaClient.getTelemetrySensorsV1Topic());
    }
}
