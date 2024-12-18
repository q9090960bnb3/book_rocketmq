package com.q9090960bnb3.demo;

import java.util.List;
import java.util.UUID;
import java.util.Date;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class IRetryTest {
    @Test
    public void retryProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("retry-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        // 生产者发送消息 重试次数
        producer.setRetryTimesWhenSendFailed(2);
        producer.setRetryTimesWhenSendAsyncFailed(2);
        String key = UUID.randomUUID().toString();
        System.out.println("key:" + key);
        Message message = new Message("retryTopic", "vip1", key, "我是vip1的文章".getBytes());
        producer.send(message);

        producer.shutdown();
        System.out.println("发送完成");
    }

    /**
     * 重试的时间间隔
     * 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * 默认重试16次
     * 如果重试了16次都是失败的？
     * 1.能否自定义重试次数？可以，这样重试次数达到自定义值后进入死信
     * 2.如果重试了16次(并发模式) 顺序模式(int最大值) 会变成死信消息，主题名称 %DLQ%retry-consumer-group
     * 3.当消息处理失败的时候该如何正确的处理？
     * 
     * @throws Exception
     */
    @Test
    public void retryConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("retryTopic", "*");
        consumer.setMaxReconsumeTimes(2);
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("date:" + new Date() + "我是vip1消费者，消费消息:" + new String(msg.getBody()));
                    System.out.println("key:" + msg.getKeys());
                }
                // 业务报错， 返回 null 或 返回 RECONSUME_LATER 都会重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        });
        consumer.start();
        System.in.read();
    }

    // 单独处理死信
    @Test
    public void retryDeadConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("dead-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("%DLQ%retry-consumer-group", "*");
        consumer.setMaxReconsumeTimes(2);
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("date:" + new Date() + "我是vip1消费者，消费消息:" + new String(msg.getBody()));
                    System.out.println("key:" + msg.getKeys());
                    System.out.println("通知人工处理");
                }
                // 业务报错， 返回 null 或 返回 RECONSUME_LATER 都会重试
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.start();
        System.in.read();
    }
}
