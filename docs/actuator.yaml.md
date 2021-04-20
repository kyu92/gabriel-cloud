# 健康监测配置

*此项配置是用于SpringBoot健康监测的配置，主要用于与Sentinel配合实现限流、熔断、降级*
***

**文件内容**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

**本文件一般无需修改*