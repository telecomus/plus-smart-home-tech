package ru.yandex.practicum.telemetry.collector.grpc.handlers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;

@Component
public class ClimateGRPCSensorEventHandler extends GRPCSensorEventHandler {
    @Autowired
    public ClimateGRPCSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected Object getPayload(SensorEventProto event) {
        final ClimateSensorProto payload = event.getClimateSensorEvent();
        return ClimateSensorAvro.newBuilder()
                .setHumidity(payload.getHumidity())
                .setCo2Level(payload.getCo2Level())
                .setTemperatureC(payload.getTemperatureC())
                .build();
    }
}
