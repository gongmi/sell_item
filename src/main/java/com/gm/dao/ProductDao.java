package com.gm.dao;

import com.gm.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    int reduceNumber(@Param("productId") long productId, @Param("killTime") Date killTime);

    Product queryById(long productId);

    List<Product> queryAll();


    /**
     * 使用存储过程执行秒杀
     */
    void killByProcedure(Map<String, Object> paramMap);
}
