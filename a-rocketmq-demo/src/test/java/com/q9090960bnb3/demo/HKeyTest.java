package com.q9090960bnb3.demo;

import java.util.List;
import java.util.UUID;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class HKeyTest {

    /**
     * key 是业务参数，用于查阅和去重
     * @throws Exception
     */
    @Test
    public void keyProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("key-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();

        String key = UUID.randomUUID().toString();
        System.out.println("key:" + key);
        Message message = new Message("keyTopic", "vip1", key, "我是vip1的文章".getBytes());
        producer.send(message);

        producer.shutdown();
        System.out.println("发送完成");
    }

    @Test
    public void tagConsumer1() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("key-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("keyTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("我是vip1消费者，消费消息:" + new String(msg.getBody()));
                    System.out.println("key:" + msg.getKeys());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.start();
        System.in.read();
    }

}
