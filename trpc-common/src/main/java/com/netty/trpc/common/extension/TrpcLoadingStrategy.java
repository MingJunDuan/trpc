package com.netty.trpc.common.extension;

public class TrpcLoadingStrategy implements LoadingStrategy {

    @Override
    public String directory() {
        return "META-INF/trpc/";
    }

    @Override
    public boolean overridden() {
        return true;
    }

}
