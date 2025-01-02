# 秒杀系统

## 分析

- 单tomcat节点 (1w/s)

- nginx (5w/s)
  - tomcat (1w/s)
  - ...

- Lvs/F5 (20w/s) 硬件
  - nginx (5w/s)
    - tomcat (1w/s)
    - ...
  - ...

- 域名/dns轮询策略 (100w/s)
  - Lvs/F5 (20w/s) 各个机房
    - nginx (5w/s)
      - tomcat (1w/s)
      - tomcat...
    - nginx...
  - Lvs/F5...

## 如何优化接口响应时间

1.能异步就异步
2.减少IO(统一查，统一写)
3.尽早return
4.加锁粒度尽可能小
5.事务控制粒度尽可能小
.....

## 秒杀设计

- 判断库存够不够如果够执行业务不够直接return
- redis 并发量
 - read 11w/-8w
 - write 8w-6w
- 消费券 
  - 1个人只能抢一次
  - 1个人针对一个商品只能抢一次
  
## 技术选择型

- Springboot +接收请求并操作redis和mysql
- Redis   用于缓存+分布式锁
- Rocketmq  用于解耦  削峰，异步
- Mysql   用于存放真实的商品信息
- Mybatis   用于操作数据库的orm框架

## 架构图

![alt text](imgs/image-6.png)

## 总结

部署细节:
  用户量：50w
  日活量：1w-2w 1%-5%
  qps: 2w+ [自己打日志丨nginx(access.log]
  几台服务器(什么配置): 8C16G 4台 seckill-web :4台 seckill-service 2台 
  带宽：100M
  技术要点：
  1.通过redis的setnx对用户和商品做去重判断，防止用户刷接口的行为
  2.每天晚上8点通过定时任务 把mysql中参与秒杀的库存商品，同步到redis中去，做库存的预扣减，提升接口性
  3.通过RocketMg消息中间件的异步消息，来将秒杀的业务异步化，进一步提升性能
  4.seckill-service使用并发消费模式，并且设置合理的线程数量，快速处理队列中堆积的消息
  5.使用redis的分布式锁+自旋锁，对商品的库存进行并发控制，把并发压力转移到程序中和redis中去，减少db
  6.使用声明式事务注解Transactional，并且设置异常回滚类型，控制数据库的原子性操作
  7.使用jmeter压测工具，对秒杀接口进行压力测试，在8C16G的服务器上，qps2k+，达到压测预期