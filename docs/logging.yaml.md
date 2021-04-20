# 日志配置

*本文件用于配置运行日志打印*
***

**文件内容**
```yaml
logging:
  level:
    root: info
    com:
      baomidou: warn
  pattern:
#    console: '%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n'
    file: '%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n'
  file:
    max-history: 30
    name: logs/${spring.application.name}_log.log
    clean-history-on-start: false
```

#### 配置项
* level: 此处用于配置各个包的日志输出等级,如com.baomidou: warn，此项配置就表示com.baomidou包下的所有类输出的日志级别都是warn
* pattern: 此处配置用于设置控制台和文件的日志格式
* max-history: 此处表示历史日志文件最多保存多少天
* name: 此处表示日志的文件名
* clean-history-on-start: 项目启动后是否清除历史日志



#### 日志等级
0. OFF
1. DEBUG
2. INFO
3. WARN
4. ERROR
5. FATAL

#### 日志等级说明:
* OFF：关闭日志  
* DEBUG：程序调试bug时使用  
* INFO：程序正常运行时使用  
* WARN：程序未按预期运行时使用，但并不是错误，如:用户登录密码错误  
* ERROR：程序出错误时使用，如:IO操作失败  
* FATAL：特别严重的问题，导致程序不能再继续运行时使用，如:磁盘空间为空，一般很少使用  
* 默认的是WARN等级，当在WARN或WARN之上等级的才记录日志信息。  
**日志等级从低到高的顺序是: DEBUG < INFO < WARN < ERROR < FATAL**