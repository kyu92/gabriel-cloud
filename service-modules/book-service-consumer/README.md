# 书籍服务消费者模块

> > 本项目所有模块采用Nacos作为注册中心和配置中心，Nacos必须在所有项目启动之前启动，如果nacos以集群模式运行，请将Nginx负载均衡后的地址作为服务器地址

### 运行方式

> 1.以jar包方式运行：
>
>   * 运行命令: java -jar book-service-consumer-0.0.1.jar
>   * 或者直接运行boot.sh(window下使用boot.cmd)
>
> 2.以docker容器方式运行
>
> * 构建镜像: docker build -t gabriel/book-service-consumer .
>
> * 运行容器: docker run -p 8011:8011 -v D:\IDEAWorkSpaces\gabriel-cloud\docker\book-service-consumer\config:/book-service-consumer/config -v D:\IDEAWorkSpaces\gabriel-cloud\docker\book-service-consumer\logs:/book-service-consumer/logs --name gabriel-book-service-consumer gabriel/book-service-consumer
>
> *注：此处name可以自行选择是否需要该参数，-p为容器需要暴露的端口，默认配置文件为8011  
> -v为容器数据卷，根目录为/book-service-consumer，推荐映射配置文件目录即可  
> /book-service-consumer/logs对应日志目录，/book-service-consumer/config对应配置目录
>

### 配置文件

> 本模块为书籍服务消费者模块，大部分配置内容都在Nacos中，需要改动的配置全部存放于bootstrap.yml文件中
> * server.port
>   > 该项配置代表服务器运行端口，如果在容器内运行，需要提前设置好端口，并在创建容器前做好映射关系
> * spring.cloud.nacos.discovery.server-addr
>   > 该项配置代表注册中心的地址，填写格式为IP:端口，例如: 127.0.0.1:8848  
    此处需要根据注册中心运行情况配置  
    一般考虑将nacos地址通过nginx负载均衡后直接填入nginx的IP和监听端口
> * spring.cloud.nacos.config.shared-configs  
>   > 该项配置用于调整公用配置类，此处填写的内容应与Nacos中配置类文件名相同