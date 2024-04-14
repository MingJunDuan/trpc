package com.netty.trpc.serialization.api;

public enum SerializerType {
    PROTOSTUFF((short) 0),HESSIANN2((short) 1),JDKNATIVE((short) 2),FAST((short) 3),KRO((short) 4),HESSIAN((short) 5);

    private short value;

    SerializerType(short value){
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}
