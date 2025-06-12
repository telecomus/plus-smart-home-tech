package ru.yandex.practicum.telemetry.collector.grpc.handlers.hub;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

import java.time.Instant;

@AllArgsConstructor
public abstract class GRPCHubEventHandler {
    private final KafkaClient kafkaClient;

    public abstract HubEventProto.PayloadCase getMessageType();

    protected abstract Object getPayload(HubEventProto event);

    public void handle(HubEventProto event) {
        Object eventPayload = getPayload(event);
        HubEventAvro payload = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(eventPayload)
                .build();

        kafkaClient.sendData(event.getHubId(), payload, kafkaClient.getTelemetryHubsV1Topic());
    }
}
