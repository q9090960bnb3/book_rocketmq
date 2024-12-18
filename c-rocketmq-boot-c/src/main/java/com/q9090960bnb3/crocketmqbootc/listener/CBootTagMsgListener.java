package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.q9090960bnb3.crocketmqbootc.domain.MsgModel;

@Component
@RocketMQMessageListener(
    topic = "bootTagTopic", 
    consumerGroup = "boot-tag-consumer-group",
    selectorType = SelectorType.TAG,
    selectorExpression = "tagA || tagB"
)
public class CBootTagMsgListener implements RocketMQListener<MessageExt>{

    @Override
    public void onMessage(MessageExt message) {
        System.out.println(new String(message.getBody()));
    }

}
