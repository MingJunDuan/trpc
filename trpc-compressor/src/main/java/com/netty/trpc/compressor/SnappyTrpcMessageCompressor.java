package com.netty.trpc.compressor;

import java.io.IOException;

public class SnappyTrpcMessageCompressor implements TrpcMessageCompressor{

    @Override
    public byte[] compress(byte[] data) {
        if (null == data || 0 == data.length) {
            return new byte[0];
        }
        try {
            return org.xerial.snappy.Snappy.compress(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] deCompress(byte[] data) {
        if (null == data || 0 == data.length) {
            return new byte[0];
        }
        try {
            return org.xerial.snappy.Snappy.uncompress(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getMessageEncoding() {
        return TprcMessageCompressorEnum.SNAPPY.getValue();
    }
}
