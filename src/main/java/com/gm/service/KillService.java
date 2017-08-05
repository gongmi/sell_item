package com.gm.service;

import com.gm.dto.Exposer;
import com.gm.dto.KillRecord;
import com.gm.entity.Product;
import com.gm.exception.RepeatKillException;
import com.gm.exception.KillCloseException;
import com.gm.exception.KillException;

import java.util.List;

/**
 * 业务接口:站在使用者(程序员)的角度设计接口
 * 1.方法定义粒度，方法定义的要非常清楚
 * 2.参数，要越简练越好
 * 3.返回类型(return 类型一定要友好/或者return异常，我们允许的异常)
 */
public interface KillService {

    List<Product> getProductList();

    Product getById(long productId);


    //再往下，是我们最重要的行为的一些接口

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     */
    Exposer exportKillUrl(long productId);


    /**
     * 执行秒杀，有可能失败，有可能成功，所以要抛出我们允许的异常
     */
    KillRecord kill(long productId, long userPhone, String md5) throws KillException;

    /**
     * 执行秒杀操作by存储过程
     */
    KillRecord executeSeckillProcedure(long productId, long userPhone, String md5);
}
