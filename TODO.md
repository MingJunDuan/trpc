# TODO list
* 参考dubbo extensionLoader，默认值从接口注解上获取
* 获取Extenion实现，通过自适应拓展实现


* 微内核+插件架构，借鉴octo-rpc和dubbo,octo-rpc很多地方也是借鉴dubbo的
* Dubbo中多协议是怎么做的，怎么实现协议转换
* 服务预热(实现的不优雅)
* grpc探索
* 自定义用户线程池
* netty支持SSL

# Done list

* Add protostuff serialzier support
* Add client service interface integrate with spring 
* 调研Zookeeper事件变更会不会主动推送变更内容
* 泛化调用的实现
* telnet协议的实现
* 支持多注册中心
* extract serialization as a module


service+version作为key，之后value是ip和port
    来做服务发现，放弃之前zk上使用的注册所有url信息
为什么需要version，因为会存在同时部署新旧版本的问题

(优先级降到)可以加上链路追踪的，接入PingCat

# Dubbo问题记录

URL是什么，一般的都是有request和response的bean来封装请求和返回数据，但是dubbo中的URL是什么

# TODO 记录

* connection管理