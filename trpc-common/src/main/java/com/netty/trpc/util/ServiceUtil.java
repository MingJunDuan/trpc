package com.netty.trpc.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:43
 */
public class ServiceUtil {
    public static final String service_connect_token="#";

    public static String serviceKey(String interfaceName,String version){
        if (StringUtils.isBlank(version)){
            return interfaceName+service_connect_token+version;
        }
        return interfaceName;
    }
}
