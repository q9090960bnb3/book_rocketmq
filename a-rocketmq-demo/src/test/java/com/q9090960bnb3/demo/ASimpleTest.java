package com.q9090960bnb3.demo;

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

public class ASimpleTest {
    @Test
    public void simpleProducer() throws Exception{
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

    ///////////////// 消费者
    @Test
    public void simpleConsumer() throws Exception{
        // 创建一个消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        // 设置nameserver地址
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 订阅一个主题 *标识订阅这个主题所有消息
        consumer.subscribe("testTopic", "*");
        // 创建一个监听器 （一直监听，异步回调方式）
        consumer.registerMessageListener(new MessageListenerConcurrently(){
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 消费消息
                System.out.println("我是消费者");
                for (MessageExt msg : msgs) {
                    System.out.println(msg.toString());
                    System.out.println("消息内容:" + new String(msg.getBody()));
                }
                System.out.println("消费上下文:" + context);
                // 返回值
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动
        consumer.start();
        // 挂起jvm
        System.in.read();
        // Thread.sleep(1000L);
    }
}
