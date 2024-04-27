package com.netty.trpc.compressor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipTrpcMessageCompressor implements TrpcMessageCompressor{

    @Override
    public byte[] compress(byte[] data) {
        if (null == data || 0 == data.length) {
            return new byte[0];
        }

        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteOutStream)) {
            gzipOutputStream.write(data);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        return byteOutStream.toByteArray();
    }

    @Override
    public byte[] deCompress(byte[] data) {
        if (null == data || 0 == data.length) {
            return new byte[0];
        }

        ByteArrayInputStream byteInStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteInStream)) {
            int readByteNum;
            byte[] bufferArr = new byte[256];
            while ((readByteNum = gzipInputStream.read(bufferArr)) >= 0) {
                byteOutStream.write(bufferArr, 0, readByteNum);
            }
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        return byteOutStream.toByteArray();
    }

    @Override
    public String getMessageEncoding() {
        return TprcMessageCompressorEnum.GZIP.getValue();
    }
}
