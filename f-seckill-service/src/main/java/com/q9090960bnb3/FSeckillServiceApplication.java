package com.q9090960bnb3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.q9090960bnb3.mapper.GoodsMapper;

@SpringBootApplication
@EnableScheduling
public class FSeckillServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FSeckillServiceApplication.class, args);

        // GoodsMapper goodsMapper = context.getBean(GoodsMapper.class);
        // StringRedisTemplate redisTemplate = context.getBean(StringRedisTemplate.class);
    }

}
