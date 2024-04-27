package com.netty.trpc.compressor;

public interface TrpcMessageCompressor extends TrpcMessageEncoding{

    byte[] compress(byte[] data);

    byte[] deCompress(byte[] data);
}
