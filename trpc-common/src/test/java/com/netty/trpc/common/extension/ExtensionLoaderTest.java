package com.netty.trpc.common.extension;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtensionLoaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test(){
        ExtensionLoader<CustomExtensionLoader> loaderExtensionLoader = new ExtensionLoader<>(CustomExtensionLoader.class);
        CustomExtensionLoader extension = loaderExtensionLoader.getExtension(CustomExtensionLoader.class.getSimpleName().toLowerCase());
        Assert.assertNotNull(extension);
    }

    @After
    public void tearDown() throws Exception {
    }
}