package com.ecut.kafka;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;


/**
 * kafka生产者
 * @author zhouwei
 */
@Component
public class KafkaProducer{
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    public  void sendMessage(String topic, String article){
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topic, article);

        //发送成功回调
        SuccessCallback<SendResult<String, String>> successCallback = result -> {
            //成功业务逻辑
            System.out.println("成功");
        };
        //发送失败回调
        FailureCallback failureCallback = ex -> {
            System.out.println("失败");
            //失败业务逻辑
            throw new RuntimeException(ex.getMessage());
        };
        listenableFuture.addCallback(successCallback, failureCallback);
    }
}
