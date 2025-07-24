package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioConditionsPK;

import java.util.List;

public interface ScenarioConditionsRepository extends JpaRepository<ScenarioCondition, ScenarioConditionsPK> {
    List<ScenarioCondition> findAllByPkScenarioId(Long scenarioId);
}
