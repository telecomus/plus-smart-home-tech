package ru.yandex.practicum.telemetry.collector.rest.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.HubEvent;

@Component
public class DeviceRemovedEventHandler extends HubEventHandler<DeviceRemovedEvent> {
    public DeviceRemovedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected Object getPayload(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    @Override
    public HubEvent.HubEventType getEventType() {
        return HubEvent.HubEventType.DEVICE_REMOVED;
    }
}
