package ru.yandex.practicum.telemetry.analyzer.service.hub.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioActionsPK;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioConditionsPK;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioActionsRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioConditionsRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedAnalyzerHandler implements AnalyzerHubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    private final ConditionRepository conditionRepository;
    private final ScenarioConditionsRepository scenarioConditionsRepository;

    private final ActionRepository actionRepository;
    private final ScenarioActionsRepository scenarioActionsRepository;

    @Override
    public void handle(HubEventAvro hubEvent) {
        String hubId = hubEvent.getHubId();
        ScenarioAddedEventAvro eventAvro = (ScenarioAddedEventAvro) hubEvent.getPayload();
        Optional<Scenario> scenarioOptional = scenarioRepository.findByHubIdAndName(hubId, eventAvro.getName());

        if (scenarioOptional.isPresent()) {
            return;
        }

        log.debug("Сохранение сценария из ивента {}", hubEvent);

        Scenario scenario = Scenario.builder()
                .hubId(hubId)
                .name(eventAvro.getName())
                .build();

        scenario = scenarioRepository.save(scenario);

        log.debug("Сценарий {} сохранен", scenario);

        for (ScenarioConditionAvro conditionAvro : eventAvro.getConditions()) {
            log.debug("Условие {}", conditionAvro);

            Integer value = conditionAvro.getValue() instanceof Boolean booleanValue
                    ? (booleanValue ? 1 : 0)
                    : (Integer) conditionAvro.getValue();

            Sensor sensor = sensorRepository.findByIdAndHubId(conditionAvro.getSensorId(), hubId)
                    .orElseThrow(() -> new RuntimeException(String.format(
                            "Сенсор с ИД %s не найден в хабе %s", conditionAvro.getSensorId(), hubId
                    )));
            Condition condition = conditionRepository.save(Condition.builder()
                    .type(conditionAvro.getType())
                    .operation(conditionAvro.getOperation())
                    .value(value)
                    .build());

            log.debug("Условие {} сохранено", condition);

            ScenarioCondition scenarioCondition = scenarioConditionsRepository.save(new ScenarioCondition(
                    new ScenarioConditionsPK(scenario, sensor, condition)
            ));

            log.debug("Связь сценарий-условие сохранена {}", scenarioCondition.getPk());
        }

        for (DeviceActionAvro actionAvro : eventAvro.getActions()) {
            log.debug("Действие {}", actionAvro);

            Sensor sensor = sensorRepository.findByIdAndHubId(actionAvro.getSensorId(), hubId)
                    .orElseThrow(() -> new RuntimeException(String.format(
                            "Сенсор с ИД %s не найден в хабе %s", actionAvro.getSensorId(), hubId
                    )));
            Action action = actionRepository.save(Action.builder()
                            .type(actionAvro.getType())
                            .value(actionAvro.getValue())
                    .build());

            log.debug("Действие {} сохранено", actionAvro);

            ScenarioAction scenarioAction = scenarioActionsRepository.save(new ScenarioAction(
                    new ScenarioActionsPK(scenario, sensor, action)
            ));

            log.debug("Связь сценарий-действие сохранено {}", scenarioAction.getPk());
        }
    }

    @Override
    public Class<?> getCheckedClass() {
        return ScenarioAddedEventAvro.class;
    }
}
