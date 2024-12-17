package com.q9090960bnb3.demo;

import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class GTagTest {
    @Test
    public void tagProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("tag-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();

        Message message = new Message("tagTopic", "vip1", "我是vip1的文章".getBytes());
        Message message2 = new Message("tagTopic", "vip2", "我是vip2的文章".getBytes());
        producer.send(message);
        producer.send(message2);

        producer.shutdown();
        System.out.println("发送完成");
    }

    @Test
    public void tagConsumer1() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag-consumer-group-a");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("tagTopic", "vip1");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("我是vip1消费者，消费消息:" + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.start();
        System.in.read();
    }

    @Test
    public void tagConsumer2() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag-consumer-group-b");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("tagTopic", "vip1 || vip2");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("我是vip2消费者，消费消息:" + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.start();
        System.in.read();
    }
}
