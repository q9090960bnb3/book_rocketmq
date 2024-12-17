package com.q9090960bnb3.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class EBatchTest {
    @Test
    public void simpleBatchProducer() throws Exception {
        // 创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("batch-producer-group");
        // 连接nameserver
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动
        producer.start();
        // 创建消息
        List<Message> msgs = Arrays.asList(
            new Message("batchTopic", "我是一组消息的A消息".getBytes()),
            new Message("batchTopic", "我是一组消息的B消息".getBytes()),
            new Message("batchTopic", "我是一组消息的C消息".getBytes())
        );
        // 发送消息
        SendResult sendResult = producer.send(msgs);
        System.out.println(sendResult.getSendStatus());
        // 关闭生产者
        producer.shutdown();
    }

    @Test
    public void simpleBatchConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("batch-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("batchTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了:" + new Date()+ " 消息个数:" + msgs.size());
                for (MessageExt msg : msgs) {
                    System.out.println(msg.toString());
                    System.out.println("消息内容:" + new String(msg.getBody()));
                }
                System.out.println("消费上下文:" + context);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.start();
        System.in.read();
    }
}
