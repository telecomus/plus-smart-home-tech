package ru.yandex.practicum.telemetry.collector.grpc.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class SwitchGRPCSensorEventHandler extends GRPCSensorEventHandler {
    @Autowired
    public SwitchGRPCSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected Object getPayload(SensorEventProto event) {
        final SwitchSensorProto payload = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(payload.getState())
                .build();
    }
}
