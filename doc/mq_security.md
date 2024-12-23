# rocketmq 安全配置

## broker 权限
1.开启acl的控制在broker.conf中开启
```ini
aclEnable=true
```
2.配置账号密码修改plain_acl.yml，其默认为

```yaml
- accessKey: rocketmq2
  secretKey: 12345678
  whiteRemoteAddress: 192.168.1.*
  # if it is admin, it could access all resources
  admin: true
```

## dashboard 权限

1. application.properties 配置

```ini
rocketmq.config.accessKey=rocketmq2
rocketmq.config.secretKey=12345678
# 允许dashboard登陆
rocketmq.config.loginRequired=true
```

2. 配置dashboard 账号密码

users/properties

```properties
admin=admin,1
```