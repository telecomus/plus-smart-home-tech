package ru.yandex.practicum.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.service.SnapshotService;
import ru.yandex.practicum.telemetry.serialization.GeneralAvroSerializer;
import ru.yandex.practicum.telemetry.serialization.deserializators.SensorEventDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private static final int CONSUME_ATTEMPT_TIMEOUT = 200;

    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final SnapshotService snapshotService;

    @Value("${kafka.topics.telemetry-sensors-v1}")
    private String telemetrySensorsTopic;

    @Value("${kafka.topics.telemetry-snapshots-v1}")
    private String telemetrySnapshotsTopic;

    @Value("${kafka.server}")
    private String kafkaServer;

    public void start() {
        KafkaConsumer<String, SensorEventAvro> consumer = getConsumer();
        KafkaProducer<String, SpecificRecordBase> producer = getProducer();

        try {
            consumer.subscribe(List.of(telemetrySensorsTopic));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            // Цикл обработки событий
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    // обрабатываем очередную запись
                    handleRecord(record, producer);
                    // фиксируем оффсеты обработанных записей, если нужно
                    manageOffsets(record, count, consumer);
                    count++;
                }
                // фиксируем максимальный оффсет обработанных записей
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы

                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private KafkaConsumer<String, SensorEventAvro> getConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, "aggregator-consumer");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregator-consumer");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new KafkaConsumer<>(config);
    }

    private KafkaProducer<String, SpecificRecordBase> getProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class.getName());
        return new KafkaProducer<>(config);
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record,
                               int count,
                               KafkaConsumer<String, SensorEventAvro> consumer) {
        // обновляем текущий оффсет для топика-партиции
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if(exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record,
                              KafkaProducer<String, SpecificRecordBase> producer) throws InterruptedException {
        log.info("топик = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        Optional<SensorsSnapshotAvro> snapshotOpt = snapshotService.updateState(record.value());

        if (snapshotOpt.isPresent()) {
            SensorsSnapshotAvro snapshot = snapshotOpt.get();

            try {
                producer.send(new ProducerRecord<>(telemetrySnapshotsTopic, snapshot.getHubId(), snapshot));
            } catch (Exception e) {
                log.error("Ошибка отправки снапшота id={}", snapshot.getHubId(), e);
            }
        }
    }
}
