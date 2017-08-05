package com.gm.dao;

import com.gm.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDao {

    /**
     * 插入购买明细,可过滤重复
     * @return 插入的行数
     */
    int insertOrder(@Param("productId") long productId, @Param("userPhone") long userPhone);


    /**
     * 根据秒杀商品的id查询Order对象(该对象携带了Product)
     */
    Order queryByProductId(@Param("productId") long productId, @Param("userPhone") long userPhone);

}
