/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.registrycenter.common;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author dmj1161859184@126.com 2021-11-23 00:31
 * @version 1.0
 * @since 1.0
 */
@Data
public class RegistryMetadata {
    private String host;
    private int port;
    private List<RpcServiceMetaInfo> serviceInfoList;

    public RegistryMetadata() {
    }

    public RegistryMetadata(String host, int port, List<RpcServiceMetaInfo> serviceInfoList) {
        this.host = host;
        this.port = port;
        this.serviceInfoList = serviceInfoList;
    }

    public RegistryMetadata(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryMetadata that = (RegistryMetadata) o;
        return port == that.port &&
                Objects.equals(host, that.host) &&
                isListEquals(serviceInfoList, that.getServiceInfoList());
    }

    private boolean isListEquals(List<RpcServiceMetaInfo> thisList, List<RpcServiceMetaInfo> thatList) {
        if (thisList == null && thatList == null) {
            return true;
        }
        if ((thisList == null && thatList != null)
                || (thisList != null && thatList == null)
                || (thisList.size() != thatList.size())) {
            return false;
        }
        return thisList.containsAll(thatList) && thatList.containsAll(thisList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, serviceInfoList.hashCode());
    }
}
