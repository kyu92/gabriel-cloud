# vue-ui

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


# nacos
## boot
**standalone**
```shell
start startup.cmd -m standalone
```
**cluster**
```shell
start startup.cmd
```

## sentinel
```shell
java -jar sentinel-dashboard-1.8.1.jar
```

# RocketMQ
**windows**
```shell
start mqnamesrv.cmd
start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true
```
**linux**
```shell
nohup ./mqnamesrv & 
nohup ./mqbroker -n localhost:9876
```