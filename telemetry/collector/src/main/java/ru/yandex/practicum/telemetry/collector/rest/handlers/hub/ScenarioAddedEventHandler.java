package ru.yandex.practicum.telemetry.collector.rest.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.ScenarioAddedEvent;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends HubEventHandler<ScenarioAddedEvent> {
    public ScenarioAddedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected Object getPayload(ScenarioAddedEvent event) {
        List<DeviceActionAvro> actions = event.getActions().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeAvro.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .toList();
        List<ScenarioConditionAvro> conditions = event.getConditions().stream()
                .map(condition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                        .setValue(condition.getValue())
                        .build())
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();
    }

    @Override
    public HubEvent.HubEventType getEventType() {
        return HubEvent.HubEventType.SCENARIO_ADDED;
    }
}
