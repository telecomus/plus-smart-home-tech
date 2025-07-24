package ru.yandex.practicum.telemetry.collector.rest.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.ScenarioRemovedEvent;

@Component
public class ScenarioRemovedEventHandler extends HubEventHandler<ScenarioRemovedEvent> {
    public ScenarioRemovedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected Object getPayload(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    @Override
    public HubEvent.HubEventType getEventType() {
        return HubEvent.HubEventType.SCENARIO_REMOVED;
    }
}
