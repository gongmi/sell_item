package com.gm.dao;

import com.gm.entity.Product;
//import com.sun.xml.internal.rngom.binary.DataExceptPattern;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit整合，这样junit在启动时就会加载spring容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ProductDaoTest {

    //注入Dao实现类依赖 idea会报错 因为它在spring容器中找不到ProductDao
//    那是因为我们是用mapper.xml来注入的ProductDao 所以它没那么智能
    @Resource
    private ProductDao productDao;


    @Test
    public void queryById() throws Exception {
        long productId = 1005;
        Product product = productDao.queryById(productId);
        System.out.println(product.getName());
        System.out.println(product);
    }

    @Test
    public void queryAll() throws Exception {

        List<Product> products = productDao.queryAll();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    public void reduceNumber() throws Exception {

        long productId = 1004;
        Date date = new Date();
        int updateCount = productDao.reduceNumber(productId, date);
        System.out.println(updateCount);

    }


}