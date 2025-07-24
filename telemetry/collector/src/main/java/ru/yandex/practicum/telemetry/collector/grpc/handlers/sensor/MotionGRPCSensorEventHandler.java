package ru.yandex.practicum.telemetry.collector.grpc.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class MotionGRPCSensorEventHandler extends GRPCSensorEventHandler {
    @Autowired
    public MotionGRPCSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    protected Object getPayload(SensorEventProto event) {
        final MotionSensorProto payload = event.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setMotion(payload.getMotion())
                .setLinkQuality(payload.getLinkQuality())
                .setVoltage(payload.getVoltage())
                .build();
    }
}
