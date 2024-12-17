package com.q9090960bnb3.demo;

import java.util.Arrays;
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
import com.q9090960bnb3.domain.MsgModel;

public class FOrderlyTest {

    private List<MsgModel> msgModels = Arrays.asList(
        new MsgModel("qwer", 1, "下单"),
        new MsgModel("qwer", 1, "短信"),
        new MsgModel("qwer", 1, "物流"),
        new MsgModel("zxcv", 2, "下单"),
        new MsgModel("zxcv", 2, "短信"),
        new MsgModel("zxcv", 2, "物流")
    );

    @Test
    public void orderlyProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("orderly-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        // 发送顺序消息 发送时确保有序，并且要发送到同一个队列下面去
        msgModels.forEach(msgModel -> {
            Message message = new Message("orderlyTopic", msgModel.toString().getBytes());
            try {
                // 发相同的订单号去相同的队列
                producer.send(message, new MessageQueueSelector() {

                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        int hashCode = msgModel.getOrderSn().hashCode();
                        int i = hashCode % mqs.size();
                        return mqs.get(i);
                    }
                    
                }, msgModel.getOrderSn());
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    
        producer.shutdown();
        System.out.println("发送完成");
    }

    @Test
    public void orderlyConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderly-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderlyTopic", "*");
        // MessageListenerConcurrently 并发模式 多线程的 重试16次 失败会放入死信队列
        // MessageListenerOrderly 顺序模式 每一组是一个线程 无限重试 Integer.MAX_VALUE
        consumer.registerMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("线程id:" + Thread.currentThread().getId());
                // System.out.println("收到消息了:" + new Date());
                for (MessageExt msg : msgs) {
                    // System.out.println(msg.toString());
                    System.out.println("消息内容:" + new String(msg.getBody()));
                }
                // System.out.println("消费上下文:" + context);
                return ConsumeOrderlyStatus.SUCCESS;
            }
            
        });
        consumer.start();
        System.in.read();
    }

    ///////////////////////// 以下错误示例，不能保证顺序性
    // @Test
    // public void orderlyProducer() throws Exception{
    //     DefaultMQProducer producer = new DefaultMQProducer("orderly-producer-group");
    //     producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
    //     producer.start();
    //     for(int i=0; i< 10; i++){
    //         Message message = new Message("orderlyTopic", ("我是第"+i+"个消息").getBytes());
    //         producer.send(message);
    //     }
    
    //     producer.shutdown();
    //     System.out.println("发送完成");
    // }

    // @Test
    // public void orderlyConsumer() throws Exception {
    //     DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderly-consumer-group");
    //     consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
    //     consumer.subscribe("orderlyTopic", "*");
    //     // MessageListenerConcurrently 并发模式
    //     consumer.registerMessageListener(new MessageListenerConcurrently() {

    //         @Override
    //         public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
    //             // System.out.println("收到消息了:" + new Date());
    //             for (MessageExt msg : msgs) {
    //                 // System.out.println(msg.toString());
    //                 System.out.println("消息内容:" + new String(msg.getBody()));
    //             }
    //             // System.out.println("消费上下文:" + context);
    //             return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    //         }

    //     });
    //     consumer.start();
    //     System.in.read();
    // }
}
