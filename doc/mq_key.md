# rocketMQ中消息的Key

- msgID 是mq给消息生产的唯一标记,也可以自己制定

```java
@Test
public void keyProducer() throws Exception {
    DefaultMQProducer producer = new DefaultMQProducer("key-producer-group");
    producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
    producer.start();

    String key = UUID.randomUUID().toString();
    System.out.println("key:" + key);
    Message message = new Message("keyTopic", "vip1", key, "我是vip1的文章".getBytes());
    producer.send(message);

    producer.shutdown();
    System.out.println("发送完成");
}

@Test
public void tagConsumer1() throws Exception {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("key-consumer-group");
    consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
    consumer.subscribe("keyTopic", "*");
    consumer.registerMessageListener(new MessageListenerConcurrently() {

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt msg : msgs) {
                System.out.println("我是vip1消费者，消费消息:" + new String(msg.getBody()));
                System.out.println("key:" + msg.getKeys());
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

    });
    consumer.start();
    System.in.read();
}
```

## key用于去重和查询

- 为什么会重复
1. 消息会重复生产者多次投递了
2. 消费者方因为扩容时会重试
   
- 所以需要在client通过key进行去重