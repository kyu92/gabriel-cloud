# Seata分布式事务组件配置

*本文件用于SpringCloud Alibaba的Seata组件配置*
***

**文件内容**
```yaml
seata:
  enabled: true
  application-id: GabrielCloud
  tx-service-group: ManagerServiceGroup
  enable-auto-data-source-proxy: true
  config:
    nacos:
      server-addr: localhost:81
      group: SEATA_GROUP
      username: nacos
      password: nacos
  registry:
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:81
      userName: nacos
      password: nacos
  service:
    vgroup-mapping:
      ManagerServiceGroup: default
    disable-global-transaction: false
  client:
    rm:
      report-success-enable: false
```

### 配置项
* application-id: 应用名
* tx-service-group: 事务分组，此处要与Seata配置的service.vgroupMapping.xxx相同
* registry.nacos: seata服务在nacos注册中心的相关配置
    * application: 在注册中心注册的应用名
    * server-addr: 注册中心地址
    * userName: 注册中心登录用户名
    * password: 注册中心登录密码
* config.nacos: seata服务在nacos配置中心的相关配置
  server-addr: 注册中心地址
  group: 配置组（一般不建议修改）
  username: 注册中心登录用户名
  password: 注册中心登录密码