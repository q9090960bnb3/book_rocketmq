package com.q9090960bnb3.brocketmqbootp;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BRocketmqBootPApplicationTests {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void contextLoads() {
        rocketMQTemplate.syncSend("bootTestTopic", "我是boot的一个消息");
    }

}
