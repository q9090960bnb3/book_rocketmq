package com.q9090960bnb3.demo;

import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class DMsTest {
    @Test
    public void msProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ms-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("orderMsTopic", "订单号，座位号".getBytes());
        // 给消息设置死亡时间 messageDelayLevel="1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m
        // 30m 1h 2h"
        message.setDelayTimeLevel(3);
        producer.send(message);
        System.out.println("发送时间:" + new Date());
        producer.shutdown();
    }
    /**
     * 发送时间:Tue Dec 17 14:56:30 CST 2024
     * 收到消息了:Tue Dec 17 14:57:02 CST 2024
     * 测试第一次可能初始化操作时间不那么准，第二次就很准了
     * 发送时间:Tue Dec 17 14:58:12 CST 2024
     * 收到消息了:Tue Dec 17 14:58:22 CST 2024
     * @throws Exception
     */
    @Test
    public void msConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ms-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderMsTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了:" + new Date());
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
