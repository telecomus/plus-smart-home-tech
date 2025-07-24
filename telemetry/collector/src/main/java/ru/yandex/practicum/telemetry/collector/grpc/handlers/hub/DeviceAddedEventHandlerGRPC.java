package ru.yandex.practicum.telemetry.collector.grpc.handlers.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class DeviceAddedEventHandlerGRPC extends GRPCHubEventHandler {
    @Autowired
    public DeviceAddedEventHandlerGRPC(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected Object getPayload(HubEventProto event) {
        final DeviceAddedEventProto payload = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(payload.getId())
                .setType(DeviceTypeAvro.valueOf(payload.getType().name()))
                .build();
    }
}
