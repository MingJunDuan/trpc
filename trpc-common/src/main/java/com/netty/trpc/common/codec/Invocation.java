/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.common.codec;

import java.util.Map;

/**
 * @author dmj1161859184@126.com 2023-08-29 00:19
 * @version 1.0
 * @since 1.0
 */
public interface Invocation {

    /**
     * get method name.
     *
     * @return method name.
     */
    String getMethodName();

    /**
     * get parameter types.
     *
     * @return parameter types.
     */
    Class<?>[] getParameterTypes();

    /**
     * get arguments.
     *
     * @return arguments.
     */
    Object[] getArguments();

    /**
     * get attachments.
     *
     * @return attachments.
     */
    Map<String, String> getAttachments();
}
