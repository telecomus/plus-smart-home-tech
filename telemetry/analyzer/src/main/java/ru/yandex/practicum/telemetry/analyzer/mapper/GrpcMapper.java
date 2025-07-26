package ru.yandex.practicum.telemetry.analyzer.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;

import java.time.Instant;

@Component
public class GrpcMapper {
    public DeviceActionProto actionProto(Sensor sensor, Action action) {
        DeviceActionProto.Builder actionProtoBuilder = DeviceActionProto.newBuilder();
        actionProtoBuilder
                .setSensorId(sensor.getId())
                .setType(ActionTypeProto.valueOf(action.getType().name()));

        if (action.getValue() != null) {
            actionProtoBuilder.setValue(action.getValue());
        }

        return actionProtoBuilder.build();
    }

    public DeviceActionRequest request(String hubId, String scenarioName, DeviceActionProto action) {
        Instant now = Instant.now();
        Timestamp ts = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        return DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(action)
                .setTimestamp(ts)
                .setTimestamp(Timestamp.newBuilder().build())
                .build();
    }
}
