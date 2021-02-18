package com.netty.trpc.common.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 12:58
 */
@Data
public class RpcProtocol implements Serializable {
    private String host;
    private int port;
    private List<RpcServiceInfo> serviceInfoList;

    public RpcProtocol() {
    }

    public RpcProtocol(String host, int port, List<RpcServiceInfo> serviceInfoList) {
        this.host = host;
        this.port = port;
        this.serviceInfoList = serviceInfoList;
    }

    public RpcProtocol(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcProtocol that = (RpcProtocol) o;
        return port == that.port &&
                Objects.equals(host, that.host) &&
                isListEquals(serviceInfoList, that.getServiceInfoList());
    }

    private boolean isListEquals(List<RpcServiceInfo> thisList, List<RpcServiceInfo> thatList) {
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
