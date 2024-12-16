# mq安装

## java环境

以java8为例

## 下载

- 二进制

```sh
wget https://archive.apache.org/dist/rocketmq/4.9.2/rocketmq-all-4.9.2-bin-release.zip
```

- dashboard
  
```sh
wget https://dist.apache.org/repos/dist/release/rocketmq/rocketmq-dashboard/1.0.0/rocketmq-dashboard-1.0.0-source-release.zip
```

## 修改

- 系统环境变量

```sh
$ vim /etc/profile

export NAMESRV_ADDR=localhost:9876
```

## mq服务安装

- 解压
  
- `rocketmq-4.9.2/bin`中

```sh
$ vim runserver.sh 

JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
```

- `rocketmq-4.9.2/bin`中

```sh
$ vim runbroker.sh

JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g"
```

- `rocketmq-4.9.2/conf`中

```sh
$ vim broker.conf

namesrvAddr=localhost:9876
autoCreateTopicEnable=true
# 外网需写公网地址
brokerIP1=127.0.0.1
```

- 启动

```sh
$ nohup sh mqnamesrv >> log/namesrv.log & 
$ jps
$ jps -l
$ nohup sh mqbroker -c ../conf/broker.conf >> log/broker.log &
```

## mq可视化工具

解压`rocketmq-dashboard-1.0.0-source-release.zip`

```sh
$ mvn clean package -Dmaven.test.skip=true
$ nohup java -jar target/rocketmq-dashboard-1.0.0.jar --server.port=8001 --rocketmq.config.namesrvAddr=127.0.0.1:9876 >> dashboard.log &
$ jps -l
```