package com.netty.trpc.compressor;

public enum TprcMessageCompressorEnum {
    BIZP2("Bizp2"),GZIP("Gzip"),IDENTITY("Identity"), SNAPPY("Snappy");
    private String value;

    TprcMessageCompressorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
