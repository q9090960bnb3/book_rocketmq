package com.q9090960bnb3.brocketmqbootp;

import java.util.Arrays;
import java.util.List;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.q9090960bnb3.brocketmqbootp.domain.MsgModel;

@SpringBootTest
class BRocketmqBootPApplicationTests {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void contextLoads() {
        // rocketMQTemplate.syncSend("bootTestTopic", "我是boot的一个消息");

        // rocketMQTemplate.asyncSend("bootAsyncTestTopic", "我是boot的一个异步消息", new SendCallback() {

        //     @Override
        //     public void onException(Throwable arg0) {
        //         System.out.println("失败" + arg0.getMessage());
        //     }

        //     @Override
        //     public void onSuccess(SendResult arg0) {
        //         System.out.println("成功");
        //     }

        // });

        // rocketMQTemplate.sendOneWay("bootOneWayTestTopic", "我是boot的一个oneway消息");

        // // 延迟消息
        // Message<String> msg = MessageBuilder.withPayload("我是一个延迟消息").build();
        // rocketMQTemplate.syncSend("bootMsTopic", msg, 3000, 3);

        // 顺序消息 发送者放消费者需要单线程消费
        List<MsgModel> msgModels = Arrays.asList(
                new MsgModel("qwer", 1, "下单"),
                new MsgModel("qwer", 1, "短信"),
                new MsgModel("qwer", 1, "物流"),
                new MsgModel("zxcv", 2, "下单"),
                new MsgModel("zxcv", 2, "短信"),
                new MsgModel("zxcv", 2, "物流")); 

        msgModels.forEach(msgModel -> {
            rocketMQTemplate.syncSendOrderly("bootOrderlyTopic", JSON.toJSONString(msgModel), msgModel.getOrderSn());
        });
    }

    @Test
    void TagKeyTest(){
        //topic:tag
        rocketMQTemplate.syncSend("bootTagTopic:tagA", "我是一个带tag的消息");

        Message<String> message = MessageBuilder.withPayload("我是一个带key的消息").setHeader(RocketMQHeaders.KEYS, "sasdfaduuid-sasd").build();
        rocketMQTemplate.syncSend("bootKeyTopic", message);
    }

}
