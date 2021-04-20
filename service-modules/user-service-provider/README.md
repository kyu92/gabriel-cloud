# 用户服务生产者模块

> 本项目所有模块采用Nacos作为注册中心和配置中心，Nacos必须在所有项目启动之前启动，如果nacos以集群模式运行，请将Nginx负载均衡后的地址作为服务器地址

### 运行方式

> 1.以jar包方式运行：
>
>   * 运行命令: java -jar user-service-provider-0.0.1.jar
>   * 或者直接运行boot.sh(window下使用boot.cmd)
>
> 2.以docker容器方式运行
>
> * 构建镜像: docker build -t gabriel/user-service-provider .
>
> * 运行容器: docker run -p 8021:8021 -v D:\IDEAWorkSpaces\gabriel-cloud\docker\user-service-provider\config:/user-service-provider/config -v D:\IDEAWorkSpaces\gabriel-cloud\docker\user-service-provider\logs:/user-service-provider/logs --name gabriel-user-service-provider gabriel/user-service-provider
>
> *注：此处name可以自行选择是否需要该参数，-p为容器需要暴露的端口，默认配置文件为8021  
> -v为容器数据卷，根目录为/user-service-provider，推荐映射配置文件目录即可  
> /user-service-provider/logs对应日志目录，/user-service-provider/config对应配置目录
>

### 配置文件

> 本模块为用户服务消费者模块，大部分配置内容都在Nacos中，需要改动的配置全部存放于bootstrap.yml文件中
> 本模块boostrap.yml配置方式与书籍服务生产者模块相同，详情请见[书籍服务消费者模块bootstrap.yml配置](../book-service-consumer/README.md)