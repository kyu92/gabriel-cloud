# API网关

> 这里采用SpringCloud Gateway作为网关，项目启动时需要有nacos作为注册中心

### 运行方式

> 1.以jar包方式运行：
>
>   * 运行命令: `java -jar api-gateway-0.0.1.jar`
>   * 或者直接运行boot.sh(window下使用boot.cmd)
>
> 2.以docker容器方式运行
>
> * 构建镜像: `docker build -t gabriel/gateway .`
>
> * 运行容器: 
> ```shell
> docker run -d -p 8100:8100 \
> -v D:\IDEAWorkSpaces\gabriel-cloud\docker\api-gateway\config:/gateway/config \
> -v D:\IDEAWorkSpaces\gabriel-cloud\docker\api-gateway\logs:/gateway/logs \
> --link gabriel-nginx \
> --name gabriel-gateway \
> gabriel/gateway
> ```
>
> *注：此处name可以自行选择是否需要该参数，-p为容器需要暴露的端口，默认配置文件为8100  
> -v为容器数据卷，根目录为/gateway，推荐映射配置文件目录即可  
> /gateway/logs对应日志目录，/gateway/config对应配置目录
> 

### 配置文件

> 在服务网关中，一般需要自定义的配置为: 
> * server.port
>   > 该项配置代表服务器运行端口，如果在容器内运行，需要提前设置好端口，并在创建容器前做好映射关系
> * spring.cloud.nacos.discovery.server-addr
>   > 该项配置代表注册中心的地址，填写格式为IP:端口，例如: 127.0.0.1:8848  
      此处需要根据注册中心运行情况配置  
      一般考虑将nacos地址通过nginx负载均衡后直接填入nginx的IP和监听端口