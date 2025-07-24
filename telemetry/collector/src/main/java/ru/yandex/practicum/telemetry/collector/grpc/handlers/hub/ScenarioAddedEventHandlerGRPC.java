package ru.yandex.practicum.telemetry.collector.grpc.handlers.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

import java.util.List;

@Component
public class ScenarioAddedEventHandlerGRPC extends GRPCHubEventHandler {
    @Autowired
    public ScenarioAddedEventHandlerGRPC(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected Object getPayload(HubEventProto event) {
        final ScenarioAddedEventProto payload = event.getScenarioAdded();
        List<DeviceActionAvro> actions = payload.getActionList().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeAvro.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .toList();

        List<ScenarioConditionAvro> conditions = payload.getConditionList().stream()
                .map(condition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                        .setValue(getConditionValue(condition))
                        .build())
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(payload.getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();
    }

    private Object getConditionValue(ScenarioConditionProto condition) {
        return switch (condition.getValueCase()) {
            case BOOL_VALUE -> condition.getBoolValue();
            case INT_VALUE -> condition.getIntValue();
            default -> null;
        };
    }
}
