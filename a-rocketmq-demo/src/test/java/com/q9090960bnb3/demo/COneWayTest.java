package com.q9090960bnb3.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

import com.q9090960bnb3.constant.MqConstant;

public class COneWayTest {
    @Test
    public void onewayProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("onewayTopic",  "日志xxx".getBytes());
        producer.sendOneway(message);
        System.out.println("成功");
        producer.shutdown();
    }
}
