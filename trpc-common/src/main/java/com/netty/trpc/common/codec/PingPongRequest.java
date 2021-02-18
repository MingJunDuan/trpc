package com.netty.trpc.common.codec;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:28
 */
public class PingPongRequest {
    public static final int BEAT_INTERVAL = 30;
    public static final int BEAT_TIMEOUT = 3 * BEAT_INTERVAL;
    public static final String BEAT_ID = "BEAT_PING_PONG";

    public static TrpcRequest BEAT_PING_REQUEST;

    static {
        BEAT_PING_REQUEST = new TrpcRequest();
        BEAT_PING_REQUEST.setRequestId(BEAT_ID);
    }
}
