package com.ecut.test;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@SpringBootTest
public class KafkaAdminClientExample {
    private static final String BOOTSTRAP_SERVERS_CONFIG = "kafka://1.117.87.146:9092";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // 指定要订阅的主题和分区
        TopicPartition topicPartition = new TopicPartition("freesofts", 2);
        consumer.assign(Collections.singletonList(topicPartition));
        AdminClient adminClient = AdminClient.create(buildAdminConfig());
        try {
            ListTopicsResult topicsResult = adminClient.listTopics();
            // 获取所有主题名称
            List<String> names = new ArrayList<>(topicsResult.names().get());
            names.forEach(System.out::println);
            // consumer.poll(Duration.ZERO) 方法不会一直等待，而是立即返回一个空记录集。
            // 因为 Duration.ZERO 表示不需要等待，直接返回缓冲区中可用的任何记录或元数据，或者立即返回空的记录集。因此，该方法不会阻塞程序。
            // 这是一个 Kafka Consumer 的方法调用。它会立即返回 Consumer 中消息缓冲区中已经可用的任何记录或元数据，
            // 或者如果没有任何可用的记录或元数据，则立即返回一个空的记录集。
            // 指定 Duration.ZERO 表示无限期地等待缓冲区中是否有记录可用，如果没有，则立即返回空记录集。
            // 通常，这种方法用于轮询 Kafka 集群以查找新的消息。
            // 缺少第一行中的 poll(Duration.ZERO) 方法会导致 seek 方法无法生效，
            // 因为 Consumer 在没有从 Kafka 集群拉取到消息之前并不知道每个分区的最新偏移量。
            // 因此，在执行 seek 方法设置分区偏移量之前，必须先调用 poll 方法，以确保 Consumer 能够获取到最新的分区偏移量。
            // 此外，poll 方法还会从 Kafka 集群获取新的消息记录，用于后续的 ConsumerRecords 处理。
            // 在这段代码中，poll 方法设置了等待时间为 0，表示立即返回可用的消息，而不是无限期等待，以避免阻塞程序。
            // 通常，poll 方法的等待时间需要根据实际应用的场景需求进行调整。
            consumer.poll(Duration.ZERO);
            // 指定要拉取的主题、分区、偏移量
            consumer.seek(topicPartition, 0);
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            adminClient.close();
        }
    }

    private static Properties buildAdminConfig() {
        Properties config = new Properties();
        config.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        return config;
    }
}
