package com.netty.trpc.trace.opentelemetry;

import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.resources.Resource;

public class TrpcTraceOpentelemetry {
    private static final String SERVICE_NAME = "serviceName";
    private static final int DEFAULT_PORT = 7879;
    private int prometheusPort = DEFAULT_PORT;

    public TrpcTraceOpentelemetry(){}

    public void trace(){
        //get service name from context
        Resource resource = Resource.getDefault().toBuilder().put(SERVICE_NAME, "dice-server").build();
        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .registerMetricReader(PrometheusHttpServer.builder().setPort(prometheusPort).build())
                .setResource(resource)
                .build();
    }
}
