package com.ecut.test;

import com.ecut.EcutStudentMangermentApplication;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest

@ContextConfiguration(classes = EcutStudentMangermentApplication.class)
public class kafka {
    @Autowired
    KafkaProducerTest kafkaProducerTest;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Test
    public void kafkaTest() throws InterruptedException {
        String topic = "freesofts";
        Integer partitions = 2;
        String data = "我是发送消息的人";
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send(topic,partitions,String.valueOf(i) ,data + i);
        }

//
//        Properties properties = new Properties();
//        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka://1.117.87.146:9092");
//        // 创建 AdminClient 对象
//        AdminClient adminClient = AdminClient.create(properties);
//
//        // 创建一个新的消费者组
//        String consumerGroupName = "my-consumer-group";
////        adminClient.createConsumerGroup(Collections.singleton(consumerGroupName));
//        System.out.println("创建 Consumer Group 成功");


    }

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Test
    public void describePartitions() {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka://1.117.87.146:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> KafkaConsumer = new KafkaConsumer<>(properties);
        // 1.订阅消息（不订阅无法拉取消息）
        KafkaConsumer.subscribe(Collections.singletonList("freesofts"));
//        //要拉区的主题及分区
        TopicPartition topicPartition = new TopicPartition("freesofts",2);
//         指定主题、分区、偏移量
        //
        KafkaConsumer.poll(Duration.ZERO);
        KafkaConsumer.seek(topicPartition, 2);
        // 拉取消息
        ConsumerRecords<String, String> records = KafkaConsumer.poll(Duration.ofMillis(1000));
        // 打印消息
        for (ConsumerRecord<String, String> record : records) {
            System.out.println(record.value());
        }
    }

//    public class EnvironmentCondition implements Condition {
//        @Override
//        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
//            // 判断当前类上是否标注了 @MyController 注解
//            if (metadata.isAnnotated(MyController.class.getName())) {
//                // 如果标注了，返回 true
//                return true;
//            }
//            // 或者
//            metadata.getAnnotations().isPresent(Cat.class.getName());
//            String environment = context.getEnvironment().getProperty("myapp.environment");
//            return "dev".equals(environment);
//        }
//    }

}
    @Component
    @SuppressWarnings(value = "unchecked")
    class KafkaProducerTest {
        @Resource
        private KafkaTemplate kafkaTemplate;

        public void send(String topic, String context) {
            kafkaTemplate.send(topic, context);
        }
    }

    @Component
    class kafkaConsumerTest {
        @KafkaListener(topics = {"test"})
        public void handleMessage(ConsumerRecord record){
            System.out.println("--------"+record.value()+"--------");
        }
    }


    class kafkaTest2 {
        public static void creatTopicTest() {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka://1.117.87.146:9092");
            AdminClient adminClient = KafkaAdminClient.create(properties);
            NewTopic newTopic = new NewTopic("freesofts", Optional.of(5), Optional.of((short) 1));
            CreateTopicsResult topics = adminClient.createTopics(Collections.singletonList(newTopic));
            System.out.println(topics.values().size());
        }

    }

