package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioActionsPK;

import java.util.List;

public interface ScenarioActionsRepository extends JpaRepository<ScenarioAction, ScenarioActionsPK> {
    List<ScenarioAction> findAllByPkScenarioId(Long scenarioId);
}
