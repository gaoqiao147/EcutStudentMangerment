package com.ecut.test;

import com.ecut.EcutStudentMangermentApplication;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = EcutStudentMangermentApplication.class)
public class kafka {
    @Autowired
    KafkaProducerTest kafkaProducerTest;

    @Test
    public void kafkaTest() throws InterruptedException {
        kafkaProducerTest.send("test", "hello");
//        //沉睡10秒
//        Thread.sleep(1000 * 10);
        kafkaProducerTest.send("test", "world");
    }
}
    @Component
    class KafkaProducerTest {
        @Autowired
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

