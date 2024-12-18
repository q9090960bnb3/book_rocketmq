package com.q9090960bnb3.crocketmqbootc.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootTestTopic", consumerGroup = "boot-test-consumer-group")
public class ABootSimpleMsgListener implements RocketMQListener<MessageExt>{

    /**
     * 这个方法就是消费者的方法
     * 如果泛型制定了固定的类型那么消息体就是我们的参数
     * eg: String 消息body内容 MessageExt 消息所有内容
     * 
     * 正常消费，就接收成功，报错则拒收
     */
    @Override
    public void onMessage(MessageExt message) {
        System.out.println(new String(message.getBody()));
    }

}
