package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = "traceTopic", 
    consumerGroup = "trace-consumer-group",
    consumeMode = ConsumeMode.CONCURRENTLY,
    enableMsgTrace = true // 开启消息轨迹
)
public class GTraceListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println("我是trace-consumer-group消费者:" + new String(message.getBody()));
    }

}
