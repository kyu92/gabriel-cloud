# 书籍服务模块数据源

*此项配置用于统一管理`书籍服务生产者模块`的数据源（即数据库连接）*
***

**文件内容**
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false
    username: root
    password: root
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      max-lifetime: 1800
```

#### 配置项
* url: 数据库的JDBC连接字符串，一般只需要更改host和port，数据库仅支持使用mysql
* username: mysql数据库用户名
* password: mysql数据库密码
* type: 数据库连接池类型，默认使用光池连接，如需更换德鲁伊等其他连接池，请自行更改源码和配置
* driver-class-name: 数据库驱动类，mysql8需要使用com.mysql.cj.jdbc.Driver，mysql5则是com.mysql.jdbc.Driver，在本项目中推荐使用mysql8
    