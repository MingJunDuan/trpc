package com.netty.trpc.client.faulttolerance;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-25 23:07
 */
public class FailBackInvoker {

    //若调用成功则直接返回
    //若失败则保存上下文，而后定时任务去发送
}
