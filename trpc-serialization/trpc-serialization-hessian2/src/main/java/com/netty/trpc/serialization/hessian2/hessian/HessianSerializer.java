package com.netty.trpc.serialization.hessian2.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.netty.trpc.common.extension.SPI;
import com.netty.trpc.serialization.api.Serializer;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:02
 */
@SPI(name = "hessian")
public class HessianSerializer implements Serializer {
    public static final HessianSerializer instance = new HessianSerializer();

    @Override
    public short type() {
        return 2;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        try {
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (data == null) {
            throw new NullPointerException();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        try {
            return clazz.cast(hessianInput.readObject());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                //quietly close
            }
        }
    }
}
