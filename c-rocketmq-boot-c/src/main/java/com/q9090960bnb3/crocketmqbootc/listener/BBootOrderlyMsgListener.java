package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.q9090960bnb3.crocketmqbootc.domain.MsgModel;

@Component
@RocketMQMessageListener(
    topic = "bootOrderlyTopic", 
    consumerGroup = "boot-orderly-consumer-group",
    consumeMode = ConsumeMode.ORDERLY,
    maxReconsumeTimes = 5
)
public class BBootOrderlyMsgListener implements RocketMQListener<MessageExt>{

    @Override
    public void onMessage(MessageExt message) {
        MsgModel msgModel = JSON.parseObject(new String( message.getBody()), MsgModel.class);
        System.out.println(msgModel);
    }

}
