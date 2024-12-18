package com.q9090960bnb3.arocketmqdemo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.UUID;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.q9090960bnb3.constant.MqConstant;


@SpringBootTest
class ARocketmqDemoApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 发消息
     * @throws Exception 
     */
    @Test
    void repeatProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("repeat-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        String key = UUID.randomUUID().toString();
        System.out.println(key);

        // 测试发送两个一样的消息
        Message m1 = new Message("repeatTopic", null, key, "扣减库存-1".getBytes());
        Message m1Repeat = new Message("repeatTopic", null, key, "扣减库存-1".getBytes());

        producer.send(m1);
        producer.send(m1Repeat);

        producer.shutdown();
        System.out.println("发送完成");

    }

    /**
     * 
CREATE TABLE `order_oper_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL,
  `order_sn` varchar(256) NOT NULL,
  `user` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_sn` (`order_sn`)
)
     * @throws Exception
     */
    @Test
    void repeatConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("repeat-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("repeatTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @SuppressWarnings("null")
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 先拿key
                for (MessageExt msg : msgs) {
                    String key = msg.getKeys();
                    // 原生操作数据库方式
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:55506/test_rocketmq?serverTimezone=GMT%2B8",  "root", "123456" );
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    PreparedStatement statement = null;
                    try {
                        statement = connection.prepareStatement("insert into order_oper_log(`type`, `order_sn`, `user`) values (1, '" + key + "', '123')");
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        int i1  = statement.executeUpdate();
                    } catch (SQLException e) {
                        if (e instanceof SQLIntegrityConstraintViolationException) {
                            // 唯一索引冲突异常
                            // 说明消息来过
                            System.out.println("重复消息");
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    

                    // // jdbcTemplate 方式 查询数据库
                    // jdbcTemplate.update("insert into order_oper_log(`type`, `order_sn`, `user`) values (1, ?, '123')", key);
                    // 新增要么成功要么报错修改要么成功,要么返回。要么报错
                    System.out.println("key:"+ key + " msg:" + new String(msg.getBody()));
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            
        });

        consumer.start();
        System.in.read();
    }


}
