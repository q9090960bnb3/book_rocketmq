package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = "jyTopic", 
    consumerGroup = "jy-consumer-group",
    messageModel = MessageModel.CLUSTERING,
    consumeThreadNumber = 24
)
public class EJyListener2 implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println("我是jy-consumer-group的第二个消费者:" + new String(message.getBody()));
    }

}
