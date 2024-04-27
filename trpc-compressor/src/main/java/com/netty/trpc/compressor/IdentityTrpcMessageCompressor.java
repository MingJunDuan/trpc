package com.netty.trpc.compressor;

public class IdentityTrpcMessageCompressor implements TrpcMessageCompressor{
    @Override
    public byte[] compress(byte[] data) {
        return data;
    }

    @Override
    public byte[] deCompress(byte[] data) {
        return data;
    }

    @Override
    public String getMessageEncoding() {
        return TprcMessageCompressorEnum.IDENTITY.getValue();
    }
}
