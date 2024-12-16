package com.q9090960bnb3.arocketmqdemo;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.q9090960bnb3.constant.MqConstant;

@SpringBootTest
class ARocketmqDemoApplicationTests {

    /**
     * 发消息
     * @throws Exception 
     */
    @Test
    void contextLoads() throws Exception {
        // 创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        // 连接nameserver
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动
        producer.start();
        // 创建消息
        Message message = new Message("testTopic", "我是一个简单的消息".getBytes());
        // 发送消息
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult.getSendStatus());
        // 关闭生产者
        producer.shutdown();
    }

}
