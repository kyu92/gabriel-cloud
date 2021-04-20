# 管理服务模块数据源

*此项配置用于统一管理`管理服务生产者模块`的数据源（即数据库连接）*
***

**文件内容**
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1/gabriel_manager?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false
    username: root
    password: root
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      max-lifetime: 1800
```

#### 配置项
*本文件配置项与`书籍服务数据源`配置项相同，详情请见[书籍服务数据源](datasource-book.yaml.md)*