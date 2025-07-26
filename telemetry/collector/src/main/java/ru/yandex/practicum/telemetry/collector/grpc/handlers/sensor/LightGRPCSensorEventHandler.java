package ru.yandex.practicum.telemetry.collector.grpc.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class LightGRPCSensorEventHandler extends GRPCSensorEventHandler {
    @Autowired
    public LightGRPCSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected Object getPayload(SensorEventProto event) {
        final LightSensorProto payload = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLuminosity(payload.getLuminosity())
                .setLinkQuality(payload.getLinkQuality())
                .build();
    }
}
