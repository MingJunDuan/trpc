package com.netty.trpc.common.serializer.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
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
public class HessianSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        if (obj==null) {
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
        if (data==null) {
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
//
//    @Override
//    public <T> byte[] serialize(T obj) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        Hessian2Output hessian2Output = new Hessian2Output(outputStream);
//        try {
//            hessian2Output.writeObject(obj);
//            hessian2Output.flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                hessian2Output.close();
//            } catch (IOException e) {
//                LOG.error(e);
//            }
//            close(outputStream);
//        }
//        return new byte[0];
//    }
//
//    @Override
//    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//        Hessian2Input hessian2Input = new Hessian2Input(inputStream);
//        try {
//            return (T) hessian2Input.readObject();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }finally {
//            try {
//                hessian2Input.close();
//            } catch (IOException e) {
//                LOG.error(e);
//            }
//            close(inputStream);
//        }
//    }

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
