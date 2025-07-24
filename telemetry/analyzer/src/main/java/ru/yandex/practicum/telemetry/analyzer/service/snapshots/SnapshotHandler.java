package ru.yandex.practicum.telemetry.analyzer.service.snapshots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.controller.HubRouterController;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioActionsRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioConditionsRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;
import ru.yandex.practicum.telemetry.analyzer.service.snapshots.checkers.AnalyzerConditionChecker;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotHandler {
    private final HubRouterController hubRouterController;

    private final List<AnalyzerConditionChecker> conditionCheckerList;
    private Map<ConditionTypeAvro, AnalyzerConditionChecker> conditionCheckerMap;

    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioConditionsRepository scenarioConditionsRepository;
    private final ScenarioActionsRepository scenarioActionsRepository;
    private final SensorRepository sensorRepository;

    public void handle(SensorsSnapshotAvro snapshotAvro) {
        prepareCheckers();

        log.debug("Пришел снапшот {}", snapshotAvro);

        String hubId = snapshotAvro.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        log.debug("Сценарии для хаба {}", scenarios);

        for (Scenario scenario : scenarios) {
            List<ScenarioCondition> scenarioConditions
                    = scenarioConditionsRepository.findAllByPkScenarioId(scenario.getId());

            log.debug("Сценарий {} имеет условия {}", scenario, scenarioConditions);

            if (isConditionsPassed(scenarioConditions, snapshotAvro)) {
                List<ScenarioAction> scenarioActions
                        = scenarioActionsRepository.findAllByPkScenarioId(scenario.getId());

                doActions(scenarioActions);
            }
        }
    }

    private boolean isConditionsPassed(List<ScenarioCondition> scenarioConditions, SensorsSnapshotAvro snapshotAvro) {
        for (ScenarioCondition scenarioCondition: scenarioConditions) {
            String sensorId = scenarioCondition.getPk().getSensor().getId();
            SensorStateAvro state = snapshotAvro.getSensorsState().get(sensorId);

            if (state == null) {
                log.error("Сенсор с ИД {} не найден", sensorId);
                return false;
            }

            Long conditionId = scenarioCondition.getPk().getCondition().getId();
            Condition condition = conditionRepository.findById(conditionId)
                    .orElseThrow(() -> new RuntimeException(String.format("Условие с ИД %d не найдено", conditionId)));

            AnalyzerConditionChecker checker = conditionCheckerMap.get(condition.getType());

            if (checker == null) {
                log.error("Не найден обработчик условий для типа {}", condition.getType());
                return false;
            }

            log.debug("Условие {} будет проверять {}", condition, state);

            if (!checker.check(condition, state)) {
                log.debug("Условие не выполнено");
                return false;
            }
        }

        log.debug("Все условия выполнены");
        return true;
    }

    private void doActions(List<ScenarioAction> scenarioActions) {
        for (ScenarioAction scenarioAction : scenarioActions) {
            Sensor sensor = scenarioAction.getPk().getSensor();
            Action action = scenarioAction.getPk().getAction();
            String scenarioName = scenarioAction.getPk().getScenario().getName();
            hubRouterController.handleDeviceAction(sensor, action, scenarioName);
        }
    }

    private void prepareCheckers() {
        if (conditionCheckerMap != null) {
            return;
        }

        conditionCheckerMap = conditionCheckerList.stream()
                .collect(Collectors.toMap(AnalyzerConditionChecker::getCheckedType, Function.identity()));
    }
}
