# Trpc project

A rpc framework based on Netty.

## Architecture

![Trpc skeleton](doc/skeleton.png)

## Contributing

Any contribution is welcome!

## Contact

Email: `dmj1161859184@126.com`


## 泛化调用


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