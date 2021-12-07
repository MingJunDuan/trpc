/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.registrycenter.common;

import java.util.Properties;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dmj1161859184@126.com 2021-12-02 23:33
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class RegistryCenterMetadata {
    private String serverList;
    private Properties properties;

    public RegistryCenterMetadata(String serverList){
        this.serverList = serverList;
    }
}
