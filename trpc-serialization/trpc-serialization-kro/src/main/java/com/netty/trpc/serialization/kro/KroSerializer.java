package com.netty.trpc.serialization.kro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.netty.trpc.serialization.api.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KroSerializer implements Serializer {
    private Kryo kryo = new Kryo();

    public KroSerializer(){
        //kro 5开始registrationRequired 的值默认为 true
        kryo.setRegistrationRequired(false);
    }

    @Override
    public short type() {
        return 0;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream, 100000000);
        kryo.writeObject(output,obj);
        byte[] bytes = output.toBytes();
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        return kryo.readObject(input,clazz);
    }
}
