package com.gm.dao.cache;

import com.gm.dao.ProductDao;
import com.gm.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by codingBoy on 17/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long id = 1001;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private ProductDao productDao;

    @Test
    public void testSeckill() throws Exception {
        //get and put

        Product product = redisDao.getSeckill(id);
        if (product == null) {
            product = productDao.queryById(id);
            if (product != null) {
                String result = redisDao.putSeckill(product);
                System.out.println(result);
                product = redisDao.getSeckill(id);
                System.out.println(product);
            }
        }
    }

}