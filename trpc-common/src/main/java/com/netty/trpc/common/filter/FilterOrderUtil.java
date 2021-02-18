package com.netty.trpc.common.filter;

import com.netty.trpc.common.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 15:49
 */
public class FilterOrderUtil {

    public static void sort(List<TrpcFilter> filters){
        if (CollectionUtils.isEmpty(filters)){
            return;
        }
        filters.sort(new Comparator<TrpcFilter>() {
            @Override
            public int compare(TrpcFilter o1, TrpcFilter o2) {
                Order order1 = o1.getClass().getAnnotation(Order.class);
                Order order2 = o2.getClass().getAnnotation(Order.class);
                int orderValue1=order1==null?0:order1.order();
                int orderValue2=order2==null?0:order2.order();
                return orderValue1-orderValue2;
            }
        });
    }
}
