package com.gm.service.impl;

import com.gm.dao.ProductDao;
import com.gm.dao.OrderDao;
import com.gm.dao.cache.RedisDao;
import com.gm.dto.Exposer;
import com.gm.dto.KillRecord;
import com.gm.entity.Product;
import com.gm.entity.Order;
import com.gm.enums.StateEnum;
import com.gm.exception.RepeatKillException;
import com.gm.exception.KillCloseException;
import com.gm.exception.KillException;
import com.gm.service.KillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KillServiceImpl implements KillService {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "shsdssljdd'l.";

    @Autowired //@Resource
    private ProductDao productDao;

    @Autowired //@Resource
    private OrderDao orderDao;

    @Autowired
    private RedisDao redisDao;

    public List<Product> getProductList() {
        return productDao.queryAll();
    }

    public Product getById(long productId) {
        return productDao.queryById(productId);
    }

    public Exposer exportKillUrl(long productId) {
        //优化点:缓存优化:超时的基础上维护一致性
        //1。访问redis

/*        Product product = redisDao.getSeckill(productId);
        if (product == null) {
            //2.访问数据库
            product = productDao.queryById(productId);
            if (product == null) {//说明查不到这个秒杀产品的记录
                return new Exposer(false, productId);
            } else {
                //3,放入redis
                redisDao.putSeckill(product);
            }

        }*/
        Product product = productDao.queryById(productId);
        if (product == null) {//说明查不到这个product
            return new Exposer(false, productId);
        }

        Date startTime = product.getStartTime();
        Date endTime = product.getEndTime();
        Date nowTime = new Date();
        //若是秒杀未开启或者已结束
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, productId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //秒杀开启，返回秒杀商品的id 与 给接口加密的md5
        String md5 = getMD5(productId);
        return new Exposer(true, md5, productId);
    }

    private String getMD5(long productId) {
        String base = productId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //秒杀是否成功，
    // 成功:减库存，增加明细；
    // 失败:抛出异常 runtime Exception，事务回滚
    @Transactional
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    public KillRecord kill(long productId, long userPhone, String md5)
            throws KillException {

        if (md5 == null || !md5.equals(getMD5(productId))) {
            //秒杀数据被重写了 rollback
            throw new KillException("seckill data rewrite");//秒杀数据被重写了
        }
        Date nowTime = new Date();
        int insertCount = orderDao.insertOrder(productId, userPhone);
        //用户重复秒杀 rollback
        if (insertCount <= 0) {
            throw new RepeatKillException("seckill repeated");
        }

        int updateCount = productDao.reduceNumber(productId, nowTime);
        if (updateCount <= 0) {
            //没有更新库存记录，说明秒杀结束/未开始 或者买完了 rollback
            throw new KillCloseException("seckill is closed/have not start");
        }

        //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
        Order order = orderDao.queryByProductId(productId, userPhone);
        return new KillRecord(productId, StateEnum.SUCCESS, order);

    }

    //下面这个方法不用加事务 因为 存储过程保证了事务 所以不用抛出异常了
    public KillRecord killByProcedure(long productId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(productId))) {
            return new KillRecord(productId, StateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productId", productId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        productDao.killByProcedure(map);
        int result = MapUtils.getInteger(map, "result", -2);
        if (result == 1) {
            Order order = orderDao.queryByProductId(productId, userPhone);
            return new KillRecord(productId, StateEnum.SUCCESS, order);
        }
        return new KillRecord(productId, StateEnum.stateOf(result));
    }
}







