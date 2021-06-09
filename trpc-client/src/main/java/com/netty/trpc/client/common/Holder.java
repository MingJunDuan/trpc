package com.netty.trpc.client.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-06-07 23:25
 */
public class Holder {
    private static Map<Tuple2, Object> cache = new HashMap<>();

    public static <T> T getServiceBean(Class<T> interfaceName, String version) {
        Object serviceBean = cache.get(new Tuple2(interfaceName, version));
        if (serviceBean != null) {
            return interfaceName.cast(serviceBean);
        }
        return null;
    }

    public static <T> void addServiceBean(Class<T> interfaceName, String version, Object bean) {
        Tuple2 tuple2 = new Tuple2(interfaceName, version);
        cache.put(tuple2, bean);
    }

    @Data
    @AllArgsConstructor
    static class Tuple2 {
        private Class interfaceNames;
        private String version;

        @Override
        public boolean equals(Object var1) {
            if (var1 == null || !(var1 instanceof Tuple2)) {
                return false;
            }
            Tuple2 tuple2 = (Tuple2) var1;
            if (tuple2.getInterfaceNames() == interfaceNames && tuple2.getVersion().equals(version)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hashCode = this.interfaceNames.hashCode() ^ this.version.hashCode();
            //Inspired by JDK HashMap.hashCode()
            return hashCode ^ (hashCode >>> 16);
        }
    }


}
