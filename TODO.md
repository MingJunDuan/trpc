# TODO list

* Add fail-over support
* 微内核+插件架构，借鉴octo-rpc和dubbo,octo-rpc很多地方也是借鉴dubbo的
* 自定义协议，考虑传入文本，图片，多媒体，文件
* Dubbo中多协议是怎么做的，怎么实现协议转换
* 注册中心接入Nacos、还有实现multicast实现广播
* 服务预热

# Done list

* Add protostuff serialzier support
* Add client service interface integrate with spring 
* 调研Zookeeper事件变更会不会主动推送变更内容
* 泛化调用的实现


service+version作为key，之后value是ip和port
    来做服务发现，放弃之前zk上使用的注册所有url信息
为什么需要version，因为会存在同时部署新旧版本的问题

nacos上使用临时节点模式
而zk上则需要手动删除?也有临时模式的，需要临时有序模式吗? 

可以加上链路追踪的，接入PingCat

# Dubbo问题记录

URL是什么，一般的都是有request和response的bean来封装请求和返回数据，但是dubbo中的URL是什么

