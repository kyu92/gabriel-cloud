# Redis内存数据库配置

*本文件用于配置Redis相关配置（主要用于缓存）*
***

**文件内容**
```yaml
spring:
  redis:
    database: 0
    password: Anzu928495870/
    sentinel:
      master: sen
      nodes: 112.126.67.3:26379,112.126.67.3:26380,112.126.67.3:26381
```

### 配置项
* database: 使用的数据库编号
* password: 数据库验证密码，若没有可不填
* master&nodes: redis哨兵模式下的配置，分配是哨兵节点名称，哨兵节点列表，用,分割

**如果不使用哨兵模式，请使用host,port代替sentinel以及其下的配置项*