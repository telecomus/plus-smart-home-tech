package ru.yandex.practicum.telemetry.collector.grpc.handlers.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class ScenarioRemovedEventHandlerGRPC extends GRPCHubEventHandler {
    @Autowired
    public ScenarioRemovedEventHandlerGRPC(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    protected Object getPayload(HubEventProto event) {
        final ScenarioRemovedEventProto payload = event.getScenarioRemoved();
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(payload.getName())
                .build();
    }
}
