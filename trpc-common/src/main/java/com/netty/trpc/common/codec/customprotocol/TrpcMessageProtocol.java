package com.netty.trpc.common.codec.customprotocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * https://www.jianshu.com/p/068f82d2cf2f
 *
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-05-11 16:57
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrpcMessageProtocol {
    private int type;
    private int length;
    private byte[] content;
}
