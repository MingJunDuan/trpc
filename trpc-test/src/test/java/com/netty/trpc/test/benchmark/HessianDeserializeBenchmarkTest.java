package com.netty.trpc.test.benchmark;

import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.protocol.RpcServiceInfo;
import com.netty.trpc.common.serializer.hessian.Hessian2Serializer;
import com.netty.trpc.common.serializer.hessian.HessianSerializer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-19 10:50
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class HessianDeserializeBenchmarkTest {
    private static HessianSerializer hessianSerializer=new HessianSerializer();
    private static Hessian2Serializer hessian2Serializer=new Hessian2Serializer();
    private static final int threadCount = 10;
    private static RpcProtocol projoBean = getPojoBean();

    private static byte[] hessianSerializeBytes = hessianSerializer.serialize(getInnerProjoBean());
    private static byte[] hessian2SerializeBytes = hessian2Serializer.serialize(getInnerProjoBean());

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(HessianDeserializeBenchmarkTest.class.getCanonicalName()).forks(1).build();
        new Runner(options).run();
    }

    private static RpcProtocol getPojoBean() {
        RpcProtocol rpcProtocol = new RpcProtocol();
        rpcProtocol.setHost("localhost");
        rpcProtocol.setPort(8087);

        List<RpcServiceInfo> rpcServiceInfoList = new LinkedList<>();
        RpcServiceInfo rpcServiceInfo=new RpcServiceInfo();
        rpcServiceInfo.setServiceName("com.netty.trpc.test.service.IHelloService");
        rpcServiceInfo.setVersion("1.3");
        rpcServiceInfoList.add(rpcServiceInfo);
        rpcServiceInfo=new RpcServiceInfo();
        rpcServiceInfo.setServiceName("com.netty.trpc.test.service.IPersonService");
        rpcServiceInfo.setVersion("1.0");
        rpcServiceInfoList.add(rpcServiceInfo);
        rpcServiceInfo=new RpcServiceInfo();
        rpcServiceInfo.setServiceName("com.netty.trpc.test.service.IHelloService");
        rpcServiceInfo.setVersion("1.1");
        rpcServiceInfoList.add(rpcServiceInfo);
        rpcProtocol.setServiceInfoList(rpcServiceInfoList);
        return rpcProtocol;
    }

    private static RpcProtocol getInnerProjoBean() {
        return projoBean;
    }

    @Benchmark
    @Threads(threadCount)
    public void testHessianSerializerDeserialize() {
        hessianSerializer.deserialize(hessianSerializeBytes,RpcProtocol.class);
    }

    @Benchmark
    @Threads(threadCount)
    public void testHessian2SerializerDeserialize() {
        hessian2Serializer.deserialize(hessian2SerializeBytes,RpcProtocol.class);
    }
}
