package com.netty.trpc.compressor;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Bizp2TrpcMessageCompressor implements TrpcMessageCompressor{

    @Override
    public byte[] compress(byte[] data) {
        if (null == data || 0 == data.length) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BZip2CompressorOutputStream cos;
        try {
            cos = new BZip2CompressorOutputStream(out);
            cos.write(data);
            cos.close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return out.toByteArray();
    }

    @Override
    public byte[] deCompress(byte[] data) {
        if (null == data || 0 == data.length) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            BZip2CompressorInputStream unZip = new BZip2CompressorInputStream(in);
            byte[] buffer = new byte[2048];
            int n;
            while ((n = unZip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return out.toByteArray();
    }

    @Override
    public String getMessageEncoding() {
        return TprcMessageCompressorEnum.BIZP2.getValue();
    }
}
