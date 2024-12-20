package com.q9090960bnb3.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class BAsyncTest {
    @Test
    public void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("asyncTopic",  "我是一个异步消息".getBytes());
        producer.send(message, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.err.println("发送失败:" + e.getMessage());
            }
            
        });

        System.out.println("我先执行");
        System.in.read();
    }
}
