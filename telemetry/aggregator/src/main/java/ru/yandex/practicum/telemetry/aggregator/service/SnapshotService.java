package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.service.equivalent.EquivalentChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SnapshotService {
    private final Map<Class<?>, EquivalentChecker> equivalentCheckerMap;
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap();

    @Autowired
    public SnapshotService(List<EquivalentChecker> equivalentCheckers) {
        equivalentCheckerMap = equivalentCheckers.stream()
                .collect(Collectors.toMap(EquivalentChecker::getCheckedClass, Function.identity()));
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String deviceId = event.getId();
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(
                hubId,
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build()
        );
        snapshots.put(snapshot.getHubId(), snapshot);
        Map<String, SensorStateAvro> stateMap = snapshot.getSensorsState();

        if (stateMap.containsKey(deviceId)) {
            SensorStateAvro state = stateMap.get(deviceId);

            if (state.getTimestamp().isAfter(event.getTimestamp())) {
                return Optional.empty();
            }

            EquivalentChecker equivalentChecker = equivalentCheckerMap.get(state.getData().getClass());

            if (equivalentChecker == null) {
                log.error("Не найден объект, сравнивающий данные датчика для класса {}", state.getData().getClass());
                return Optional.empty();
            }

            if (equivalentChecker.isEquivalent(state.getData(), event.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro state = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        stateMap.put(deviceId, state);
        snapshot.setTimestamp(event.getTimestamp());
        return Optional.of(snapshot);
    }
}
