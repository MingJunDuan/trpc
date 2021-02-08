package com.netty.trpc;

import org.junit.After;
import org.junit.Before;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 10:03
 */
public class BaseTest {
    protected String zkConnectStr="localhost:2181";
    protected String zkNamespaceStr="netty-trpc-test";

    @Before
    public void before() {
        doBefore();
    }

    protected void doBefore() {
    }

    @After
    public void after(){
        doAfter();
    }

    protected void doAfter() {
    }
}
