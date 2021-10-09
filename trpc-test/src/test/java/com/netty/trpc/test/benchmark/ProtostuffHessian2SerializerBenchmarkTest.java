package com.netty.trpc.test.benchmark;

import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.protocol.RpcServiceInfo;
import com.netty.trpc.common.serializer.Serializer;
import com.netty.trpc.common.serializer.hessian.Hessian2Serializer;
import com.netty.trpc.common.serializer.dyuprotostuff.DyuProtostuffSerializer;
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
 * @date 2021-02-22 10:10
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ProtostuffHessian2SerializerBenchmarkTest {
    private static final int threadCount = 8;
    private static Serializer protostuffSerializer = new DyuProtostuffSerializer();
    private static Serializer hessian2Serializer = new Hessian2Serializer();
    private static RpcProtocol projoBean = getPojoBean();

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(ProtostuffHessian2SerializerBenchmarkTest.class.getCanonicalName()).forks(1).build();
        new Runner(options).run();
    }

    private static RpcProtocol getPojoBean() {
        RpcProtocol rpcProtocol = new RpcProtocol();
        rpcProtocol.setHost("localhost");
        rpcProtocol.setPort(8087);

        List<RpcServiceInfo> rpcServiceInfoList = new LinkedList<>();
        RpcServiceInfo rpcServiceInfo = new RpcServiceInfo();
        rpcServiceInfo.setServiceName("com.netty.trpc.test.interfaces.IHelloService");
        rpcServiceInfo.setVersion("1.3");
        rpcServiceInfoList.add(rpcServiceInfo);
        rpcServiceInfo = new RpcServiceInfo();
        rpcServiceInfo.setServiceName("com.netty.trpc.test.interfaces.IPersonService");
        rpcServiceInfo.setVersion("1.0");
        rpcServiceInfoList.add(rpcServiceInfo);
        rpcServiceInfo = new RpcServiceInfo();
        rpcServiceInfo.setServiceName("com.netty.trpc.test.interfaces.IHelloService");
        rpcServiceInfo.setVersion("1.1");
        rpcServiceInfoList.add(rpcServiceInfo);
        rpcProtocol.setServiceInfoList(rpcServiceInfoList);
        return rpcProtocol;
    }

    @Benchmark
    @Threads(threadCount)
    public void testProtostuffSerializerSerialize() {
        protostuffSerializer.serialize(projoBean);
    }

    @Benchmark
    @Threads(threadCount)
    public void testHessian2SerializerSerialize() {
        hessian2Serializer.serialize(projoBean);
    }

}
