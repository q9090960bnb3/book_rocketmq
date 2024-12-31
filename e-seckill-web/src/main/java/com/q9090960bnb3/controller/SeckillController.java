package com.q9090960bnb3.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class SeckillController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    AtomicInteger userIDAt = new AtomicInteger(0);

    /**
     * 秒杀
     * 1. 用户去重
     * 2. 库存的预扣减
     * 3. 消息放入mq
     * @param goodsId
     * @param userId
     * @return
     */
    @GetMapping("/seckill")
    public String doSeckill(Integer goodsId) {
        int userId = userIDAt.incrementAndGet();
        // unqiue key
        String uk = userId + "-" + goodsId;
        // setnx
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("uk:" + uk, "");
        if (!flag) {
            return "您已经参与过该商品的抢购, 请参与其他商品O(∩_∩)O哈哈~";
        }
        
        // TODO 预扣减库存
        // 安全方式
        Long count = redisTemplate.opsForValue().decrement("goodsId:" + goodsId);
        if (count < 0){
            return "该商品已经被抢完了, 下次早点来";
        }

        // 非安全方式
        // String s = redisTemplate.opsForValue().get(goodsId);
        // int count = Integer.parseInt(s);
        // count--;
        // if (count < 0){
        //     return "该商品已经被抢完了";
        // }
        // redisTemplate.opsForValue().set(String.valueOf(goodsId), String.valueOf(count));

        rocketMQTemplate.asyncSend("seckillTopic", uk, new SendCallback() {

            @Override
            public void onException(Throwable arg0) {
                System.out.println("抢购失败:" +arg0.getMessage());
                System.out.println("用户ID:" + userId + " 商品ID:" + goodsId);
            }

            @Override
            public void onSuccess(SendResult arg0) {
                System.out.println("发送成功");

            }
            
        });

        return "正在拼命抢购中，请稍后在订单中心查看";
    }
    
    /**普通版 */
    // @GetMapping("/seckill")
    // public String doSeckill(Integer goodsId, Integer userId) {
    //     // unqiue key
    //     String uk = userId + "-" + goodsId;
    //     // setnx
    //     Boolean flag = redisTemplate.opsForValue().setIfAbsent(uk, "");
    //     if (!flag) {
    //         return "您已经参与过该商品的抢购, 请参与其他商品O(∩_∩)O哈哈~";
    //     }
        
    //     // TODO 预扣减库存
    //     // 安全方式
    //     Long count = redisTemplate.opsForValue().decrement("goodsId:" + goodsId);
    //     if (count < 0){
    //         return "该商品已经被抢完了, 下次早点来";
    //     }

    //     // 非安全方式
    //     // String s = redisTemplate.opsForValue().get(goodsId);
    //     // int count = Integer.parseInt(s);
    //     // count--;
    //     // if (count < 0){
    //     //     return "该商品已经被抢完了";
    //     // }
    //     // redisTemplate.opsForValue().set(String.valueOf(goodsId), String.valueOf(count));

    //     rocketMQTemplate.asyncSend("seckillTopic", uk, new SendCallback() {

    //         @Override
    //         public void onException(Throwable arg0) {
    //             System.out.println("抢购失败:" +arg0.getMessage());
    //             System.out.println("用户ID:" + userId + " 商品ID:" + goodsId);
    //         }

    //         @Override
    //         public void onSuccess(SendResult arg0) {
    //             System.out.println("发送成功");

    //         }
            
    //     });

    //     return "正在拼命抢购中，请稍后在订单中心查看";
    // }
}
