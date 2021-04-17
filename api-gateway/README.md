# API网关

> 这里采用SpringCloud Gateway作为网关，项目启动时需要有nacos作为注册中心

### 运行方式

> 1.以jar包方式运行：
>
>   * 运行命令: java -jar api-gateway.jar --spring.cloud.nacos.discovery.server-addr=nacos_addr
>
> *注: nacos_addr是指当前Nacos服务器的地址，例如：192.168.1.2:8848*
>
> 2.以docker容器方式运行
>
> * 构建镜像: docker build -t gabriel/gateway:latest .
>
> * 运行容器: docker run -d --name gateway -p 8100:8100 -e NACOS_ADDR=192.168.1.2:8848 gabriel/gateway:latest
>
> *注：此处name可以自行选择是否需要该参数，容器默认暴露8100端口，可以使用-p来映射，-e NACOS_ADDR必须添加，且应填写对应的nacos服务器地址*