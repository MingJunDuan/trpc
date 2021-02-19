package com.netty.trpc.common.serializer.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:02
 */
public class Hessian2Serializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(outputStream);
        try {
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                hessian2Output.close();
            } catch (IOException e) {
                LOG.error(e);
            }
            close(outputStream);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(inputStream);
        try {
            return (T) hessian2Input.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                hessian2Input.close();
            } catch (IOException e) {
                LOG.error(e);
            }
            close(inputStream);
        }
    }

    private void close(Closeable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
    }
}
