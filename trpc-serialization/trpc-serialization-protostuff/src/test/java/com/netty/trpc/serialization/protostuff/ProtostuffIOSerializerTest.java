package com.netty.trpc.serialization.protostuff;

import com.netty.trpc.common.extension.ExtensionLoader;
import com.netty.trpc.serialization.api.Serializer;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProtostuffIOSerializerTest {

    @Test
    public void serialize() {
        ExtensionLoader<Serializer> extensionLoader = new ExtensionLoader<>(Serializer.class);
        Serializer jdkNative = extensionLoader.getExtension("protobuffer");
        Assert.assertNotNull(jdkNative);

    }
}