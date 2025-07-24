package ru.yandex.practicum.telemetry.collector.grpc.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class TemperatureGRPCSensorEventHandler extends GRPCSensorEventHandler {
    @Autowired
    public TemperatureGRPCSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    protected Object getPayload(SensorEventProto event) {
        final TemperatureSensorProto payload = event.getTemperatureSensorEvent();
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(payload.getTemperatureC())
                .setTemperatureF(payload.getTemperatureF())
                .build();
    }
}
