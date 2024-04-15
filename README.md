# Trpc project

A RPC framework based on Netty.

## Architecture

![Trpc skeleton](doc/trpc.png)

## Contributing

Any contribution is welcome!


## 环境
JDK8+,Zookeeper 3.6,Nacos 1.4.4

## 功能

* ZK注册中心
* 负载均衡
* 路由支持
* 自动发现
* 自定义RPC协议
* 泛化调用
* 支持接口多版本
* 支持自定义协议
* 支持多注册中心(zookeeper和nacos)
* Connection管理(TODO)

## TPS
框架默认使用Fast序列化协议

### Fast序列化
```
QPS接近5w左右，具体实现见：com.netty.trpc.test.tps.TrpcClientBootstrapTest

2024-04-14 10:47:48.130 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:48470 
2024-04-14 10:47:49.135 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:52124
2024-04-14 10:47:50.139 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:50084
2024-04-14 10:47:51.144 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:49294
2024-04-14 10:47:52.149 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:49203
2024-04-14 10:47:53.154 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:42943
2024-04-14 10:47:54.159 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:47514
2024-04-14 10:47:55.165 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:49053
2024-04-14 10:47:56.168 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:50386
2024-04-14 10:47:57.173 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:51647
2024-04-14 10:47:58.178 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:51384
2024-04-14 10:47:59.183 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:51200
```
### Protostuff序列化
```
QPS能达到4w左右，具体实现见：com.netty.trpc.test.tps.TrpcClientBootstrapTest

2024-04-14 09:53:36.687 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:48328
2024-04-14 09:53:37.692 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:44686
2024-04-14 09:53:38.697 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:46780
2024-04-14 09:53:39.702 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:48085
2024-04-14 09:53:40.705 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:46796
2024-04-14 09:53:41.710 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:49113
2024-04-14 09:53:42.713 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:44150
2024-04-14 09:53:43.718 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:40961
2024-04-14 09:53:44.723 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:45640
2024-04-14 09:53:45.729 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41468
2024-04-14 09:53:46.730 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41819
2024-04-14 09:53:47.735 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:40550
2024-04-14 09:53:48.736 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:42277
2024-04-14 09:53:49.741 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:30436
2024-04-14 09:53:50.747 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:39527
```
### Fury序列化
```
2024-04-16 00:01:50.453 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:39248 
2024-04-16 00:01:51.457 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:35633 
2024-04-16 00:01:52.458 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:34915 
2024-04-16 00:01:53.459 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:31412 
2024-04-16 00:01:54.463 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:32016 
2024-04-16 00:01:55.468 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:46048 
2024-04-16 00:01:56.472 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:45575 
2024-04-16 00:01:57.477 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:45941 
2024-04-16 00:01:58.482 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41835 
2024-04-16 00:01:59.484 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:39222 
```
### Hessian2序列化
```
2024-04-16 00:05:03.183 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41654 
2024-04-16 00:05:04.188 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:38499 
2024-04-16 00:05:05.193 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:42100 
2024-04-16 00:05:06.197 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41327 
2024-04-16 00:05:07.202 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:42158 
2024-04-16 00:05:08.207 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:40033 
2024-04-16 00:05:09.212 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41962 
2024-04-16 00:05:10.213 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:41748 
2024-04-16 00:05:11.217 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:40942 
2024-04-16 00:05:12.221 [Thread-1] INFO  c.n.t.t.tps.TrpcClientBootstrapTest run() 60 TPS:39570 
```

# 泛化调用

泛化调用实现方式有俩种：

1.客户端框架编译出要调用的类，获取Class然后传到服务端

2.客户端只是带上String类型的类名、参数类型到服务端，服务端再通过ClassLoader进行load，然后进行反射调用

如何使用：
```bash
public interface IPersonService {

    List<Person> callPerson(String name, Integer num);


    Set<Person> getPersonSet(HashMap<String,Integer> map,LinkedList<String> names);
}
```

```bash
    private TrpcClient trpcClient;

    @Before
    public void before() {
        trpcClient = new TrpcClient(getRegistryAddress());
    }
    
    //泛化调用
    @Test
    public void test_generic() throws InterruptedException {
        GenericReference genericReference = new GenericReference();
        genericReference.setTrpcClient(trpcClient);
        genericReference.setInterfaceName("com.netty.trpc.test.api.IPersonService");
        genericReference.setVersion("1.2");
        genericReference.setMethodName("callPerson");
        String[] parameterTypes = {"java.lang.String", "java.lang.Integer"};
        genericReference.setParameterTypes(parameterTypes);

        GenericConfig genericConfig = genericReference.get();
        Object[] args = {"Jack", 3};
        for (int i = 0; i < 10; i++) {
            Object result = genericConfig.$invoke(args);
            LOGGER.info("result:{}",result.toString());
        }
    }

    @Test
    public void test_genericComplicatedParameters(){
        GenericReference genericReference = new GenericReference();
        genericReference.setTrpcClient(trpcClient);
        genericReference.setInterfaceName("com.netty.trpc.test.api.IPersonService");
        genericReference.setVersion("1.2");
        genericReference.setMethodName("getPersonSet");
        String[] parameterTypes = {"java.util.HashMap", "java.util.LinkedList"};
        genericReference.setParameterTypes(parameterTypes);

        GenericConfig genericConfig = genericReference.get();
        List<String> list=new LinkedList<>();
        list.add("name1");
        list.add("name2");
        list.add("name3");
        HashMap<String,Integer> map=new HashMap<>();
        map.put("name1",5);
        map.put("name2",6);
        map.put("name3",7);

        Object[] args = {map, list};
        for (int i = 0; i < 10; i++) {
            Object result = genericConfig.$invoke(args);
            LOGGER.info("result:{}",result.toString());
        }
    }
```

## 开发
    
1.zookeeper3.5.9

2.nacos1.4.4


## JDK21 Virtual Thread支持

1. Idea启动时添加如下JVM参数

```bash
    Run—>EditConfigurations…—>Modify options—>Add VM options—>JVM options在JVM options 内添加下面指令：
    
    --add-opens java.base/java.lang=ALL-UNNAMED
    --add-opens java.base/java.util=ALL-UNNAMED
    --add-opens java.base/java.nio=ALL-UNNAMED
    --add-opens java.base/sun.nio.ch=ALL-UNNAMED
```

2.修改TrpcServer业务线程池使用VirtualThread