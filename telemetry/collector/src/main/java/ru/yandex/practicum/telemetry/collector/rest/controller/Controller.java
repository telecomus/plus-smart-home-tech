package ru.yandex.practicum.telemetry.collector.rest.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.rest.handlers.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.rest.handlers.sensor.SensorEventHandler;
import ru.yandex.practicum.telemetry.collector.rest.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.model.sensor.SensorEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/events")
public class Controller {
    private final Map<HubEvent.HubEventType, HubEventHandler> hubHandlers;
    private final Map<SensorEvent.SensorEventType, SensorEventHandler> sensorHandlers;

    @Autowired
    public Controller(List<HubEventHandler> hubHandlersList, List<SensorEventHandler> sensorHandlersList) {
        hubHandlers = hubHandlersList.stream()
                .collect(Collectors.toMap(HubEventHandler::getEventType, Function.identity()));
        sensorHandlers = sensorHandlersList.stream()
                .collect(Collectors.toMap(SensorEventHandler::getEventType, Function.identity()));
    }

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        SensorEvent.SensorEventType type = event.getType();

        if (!sensorHandlers.containsKey(type)) {
            throw new RuntimeException(String.format("No handler for sensor event type %s", type));
        }

        sensorHandlers.get(type).handle(event);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        HubEvent.HubEventType type = event.getType();

        if (!hubHandlers.containsKey(type)) {
            throw new RuntimeException(String.format("No handler for hub event type %s", type));
        }

        hubHandlers.get(type).handle(event);
    }
}
