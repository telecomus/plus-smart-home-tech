package ru.yandex.practicum.telemetry.analyzer.controller;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.telemetry.analyzer.mapper.GrpcMapper;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;

@Slf4j
@Service
public class HubRouterController {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;
    private final GrpcMapper grpcMapper;

    public HubRouterController(
            @GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
            GrpcMapper grpcMapper
    ) {
        this.hubRouterClient = hubRouterClient;
        this.grpcMapper = grpcMapper;
    }

    public void handleDeviceAction(Sensor sensor, Action action, String scenarioName) {
        try {
            DeviceActionProto actionProto = grpcMapper.actionProto(sensor, action);
            DeviceActionRequest request = grpcMapper.request(sensor.getHubId(), scenarioName, actionProto);
            hubRouterClient.handleDeviceAction(request);
        } catch (Exception e) {
            log.error("Ошибка handleDeviceAction при отправке команды через gRPC: {}", e.getMessage());
        }
    }
}
