package com.ecut.kafka;

import com.alibaba.fastjson.JSON;
import com.ecut.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * kafka消费者
 *
 * @author zhouwei
 */
@Component
@Slf4j
public class MyKafkaConsumer {
//    @Value("${management.kafka.consumer}")
//    private Map<String,Object> kafkaConfig;
//    @Resource
//    ElasticsearchMapper elasticsearchMapper;

        @KafkaListener(topics = {"ecut"})
    public void receiveMessage(ConsumerRecord<String, String> record) {
        if (record.value() == null) {
            log.error("接收数据为空！");
            return;
        }
            System.out.println("--------------------------------------------------------------"+record.value());
//        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(kafkaConfig);
//        kafkaConsumer.subscribe(Arrays.asList("article"));
//        // 拉去消息
//        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
//        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
//            String messageValue = consumerRecord.value();
//        }
//        // 获取kafka消费的分区信息
//        Set<TopicPartition> assignment = kafkaConsumer.assignment();
//        for (TopicPartition topicPartition : assignment) {
//        }
//        Article article = JSON.parseObject(record.value().toString(), Article.class);
//        elasticsearchMapper.save(article);
        log.info("保存到es中了");
    }
}
