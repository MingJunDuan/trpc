# Trpc project

A rpc framework based on Netty.

## Architecture

![Trpc skeleton](doc/trpc.png)

## Contributing

Any contribution is welcome!


## 环境
JDK8+,Zookeeper3.6

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

    具体实现见：com.netty.trpc.test.tps.TrpcClientBootstrapTest

    2022-03-30 01:00:13.720 [Thread-1] INFO   TPS:29778 
    2022-03-30 01:00:14.725 [Thread-1] INFO   TPS:35013 
    2022-03-30 01:00:15.728 [Thread-1] INFO   TPS:35403 
    2022-03-30 01:00:16.729 [Thread-1] INFO   TPS:37025 
    2022-03-30 01:00:17.733 [Thread-1] INFO   TPS:36795 
    2022-03-30 01:00:18.737 [Thread-1] INFO   TPS:38758 
    2022-03-30 01:00:19.742 [Thread-1] INFO   TPS:38681 
    2022-03-30 01:00:20.745 [Thread-1] INFO   TPS:39180 
    2022-03-30 01:00:21.748 [Thread-1] INFO   TPS:39349 
    2022-03-30 01:00:22.749 [Thread-1] INFO   TPS:37372 
    2022-03-30 01:00:23.752 [Thread-1] INFO   TPS:36349 
    2022-03-30 01:00:24.754 [Thread-1] INFO   TPS:38977 
    2022-03-30 01:00:25.757 [Thread-1] INFO   TPS:35857 
    2022-03-30 01:00:26.761 [Thread-1] INFO   TPS:39295 
    2022-03-30 01:00:27.766 [Thread-1] INFO   TPS:36892 

泛化调用

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