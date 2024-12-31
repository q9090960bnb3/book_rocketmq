package com.q9090960bnb3.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.q9090960bnb3.service.GoodsService;

@Component
@RocketMQMessageListener(topic = "seckillTopic", consumerGroup = "seckill-consumer-group", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadNumber = 40)
public class SeckillListener implements RocketMQListener<MessageExt>{

    @Autowired
    private GoodsService goodsService;

    /**
     * 扣减库存
     * 写订单表
     */
    /**
     * 方案1, 但不支持分布式
     */
    // @Override
    // public void onMessage(MessageExt message) {
    //     String msg = new String(message.getBody());
    //     Integer userId = Integer.parseInt(msg.split("-")[0]);
    //     Integer goodsId = Integer.parseInt(msg.split("-")[1]);

    //     // 这样可以解决并发问题，先锁再mysql 事务
    //     synchronized(this) {
    //         goodsService.realSeckill(userId, goodsId);
    //     }
        
    //     // goodsService.realSeckill(userId, goodsId);
    // }

    /**
     * 方案2 分布式锁，mysql（行锁） redis
     */
    @Override
    public void onMessage(MessageExt message) {
        String msg = new String(message.getBody());
        Integer userId = Integer.parseInt(msg.split("-")[0]);
        Integer goodsId = Integer.parseInt(msg.split("-")[1]);

        // 这样可以解决并发问题，先锁再mysql 事务
        synchronized(this) {
            goodsService.realSeckill(userId, goodsId);
        }
        
        // goodsService.realSeckill(userId, goodsId);
    }

}