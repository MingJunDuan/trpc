package com.netty.trpc.common.extension;

import com.netty.trpc.common.exception.CustomTrpcRuntimeException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtensionLoaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = CustomTrpcRuntimeException.class)
    public void test(){
        ExtensionLoader<CustomExtensionLoader> loaderExtensionLoader = new ExtensionLoader<>(CustomExtensionLoader.class);
        CustomExtensionLoader extension = loaderExtensionLoader.getExtension(CustomExtensionLoaderImpl.class.getSimpleName().toLowerCase());
        Assert.assertNull(extension);
    }

    @Test
    public void test_default(){
        ExtensionLoader<CustomExtensionLoader> loaderExtensionLoader = new ExtensionLoader<>(CustomExtensionLoader.class);
        CustomExtensionLoader extension = loaderExtensionLoader.getExtension(DefaultCustomExtensionLoaderImpl.class.getSimpleName().toLowerCase());
        Assert.assertNotNull(extension);

    }

    @Test
    public void test_success(){
        ExtensionLoader<CustomExtensionLoader> loaderExtensionLoader = new ExtensionLoader<>(CustomExtensionLoader.class);
        CustomExtensionLoader customExtensionLoaderImpl = loaderExtensionLoader.getExtension("customExtensionLoaderImpl");
        Assert.assertNotNull(customExtensionLoaderImpl);
    }


    @After
    public void tearDown() throws Exception {
    }
}