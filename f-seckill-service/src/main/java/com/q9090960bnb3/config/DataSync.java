package com.q9090960bnb3.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.q9090960bnb3.domain.Goods;
import com.q9090960bnb3.mapper.GoodsMapper;

/**
 * 1.每天10点晚上8点通过定时任务将mysqL的库存同步到redis中去
 * 2.为了测试方便希望项目启动的时候就同步数据
 */
@Component
public class DataSync {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    // 1s后执行，然后1s间隔执行
    // @Scheduled(initialDelay = 1000,fixedDelay = 1000)

    // cron 表达式
    // @Scheduled(cron = "0 0 10 0 0 ?")
   
    /**
     * 我希望这个方法再项目启动以后
     *并且再这个类的属性注入完毕以后执行

     bean生命周期:
        实例化new
        属生赋值
        初始化（前PostConstruct/中InitiaLizingBean/后BeanPostProcessor）自定义的一个initMethod方法
        使用
        销毁
    */
    @PostConstruct
    public void initData(){
        List<Goods> goodsList= goodsMapper.selectSeckillGoods();
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }
        goodsList.forEach(goods -> {
            redisTemplate.opsForValue().set("goodsId:"+goods.getId(),goods.getStocks().toString());  
        });
    }
}
