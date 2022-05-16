/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.server.executor;

import java.util.concurrent.Executor;

/**
 * @author dmj1161859184@126.com 2022-05-16 23:11
 * @version 1.0
 * @since 1.0
 */
public interface UserServiceExecutor {

    Executor getExecutor();
}
