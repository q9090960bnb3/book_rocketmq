package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = "modeTopic", 
    consumerGroup = "mode-consumer-group-a",
    messageModel = MessageModel.CLUSTERING
)
public class DC3 implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println("我是mode-consumer-group-a的第三个消费者:" + new String(message.getBody()));
    }

}
