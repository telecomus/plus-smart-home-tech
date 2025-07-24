package ru.yandex.practicum.telemetry.analyzer.service.hub.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedAnalyzerHandler implements AnalyzerHubEventHandler {
    private final ScenarioRepository scenarioRepository;

    @Override
    public void handle(HubEventAvro hubEvent) {
        ScenarioRemovedEventAvro eventAvro = (ScenarioRemovedEventAvro) hubEvent.getPayload();

        scenarioRepository.findByHubIdAndName(hubEvent.getHubId(), eventAvro.getName())
                .ifPresent(scenarioRepository::delete);
    }

    @Override
    public Class<?> getCheckedClass() {
        return ScenarioRemovedEventAvro.class;
    }
}
