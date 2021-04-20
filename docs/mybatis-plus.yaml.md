# Mybatis-plus 配置项

*本文件用于配置Mybatis-plus*
***
**文件内容**
```yaml
mybatis-plus:
  type-aliases-package: org.spring.springboot.domain
  mapper-locations: classpath:mapper/**/*.xml
  global-config:
    banner: false
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

**本文件一般无需修改*