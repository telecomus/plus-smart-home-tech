package ru.yandex.practicum.telemetry.collector.rest.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.HubEvent;

@Component
public class DeviceAddedEventHandler extends HubEventHandler<DeviceAddedEvent> {
    public DeviceAddedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected Object getPayload(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();
    }

    @Override
    public HubEvent.HubEventType getEventType() {
        return HubEvent.HubEventType.DEVICE_ADDED;
    }
}
