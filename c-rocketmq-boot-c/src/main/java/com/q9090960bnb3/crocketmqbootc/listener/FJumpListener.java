package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = "jumpTopic", 
    consumerGroup = "jump-consumer-group",
    consumeMode = ConsumeMode.CONCURRENTLY
)
public class FJumpListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println("我是jump-consumer-group消费者:" + new String(message.getBody()));
    }

}
