package ru.yandex.practicum.telemetry.analyzer.service.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.service.hub.handlers.AnalyzerHubEventHandler;
import ru.yandex.practicum.telemetry.serialization.deserializators.HubEventDeserializer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HubEventProcessor implements Runnable {
    private static final int CONSUME_ATTEMPT_TIMEOUT = 200;
    private final Map<Class<?>, AnalyzerHubEventHandler> classAnalyzerHubEventHandlerMap;

    @Value("${kafka.topics.telemetry-hubs-v1}")
    private String telemetryHubsTopic;

    @Value("${kafka.consumer.groups.hub-events}")
    private String consumerGroup;

    @Value("${kafka.server}")
    private String kafkaServer;

    @Autowired
    public HubEventProcessor(List<AnalyzerHubEventHandler> analyzerHubEventHandlers) {
        classAnalyzerHubEventHandlerMap = analyzerHubEventHandlers.stream()
                .collect(Collectors.toMap(AnalyzerHubEventHandler::getCheckedClass, Function.identity()));
    }

    @Override
    public void run() {
        KafkaConsumer<String, HubEventAvro> consumer = createConsumer();

        try {
            consumer.subscribe(List.of(telemetryHubsTopic));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEvent = record.value();

                    log.debug("Обработка события хаба {}", hubEvent);

                    try {
                        Class<?> paycloadClass = hubEvent.getPayload().getClass();
                        AnalyzerHubEventHandler hubEventHandler
                                = classAnalyzerHubEventHandlerMap.get(paycloadClass);

                        if (hubEventHandler == null) {
                            log.error("Не найден обработчик события хабов для класса {}", paycloadClass);
                            continue;
                        }

                        log.debug("Обработка класса {}", paycloadClass);
                        hubEventHandler.handle(hubEvent);
                    } catch (Exception e) {
                        log.error("Ошибка обработки HubEvent: {}", e.getMessage(), e);
                    }
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try (consumer) {
                consumer.commitSync();
            }
        }
    }

    private KafkaConsumer<String, HubEventAvro> createConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerGroup);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class.getName());
        return new KafkaConsumer<>(config);
    }
}
