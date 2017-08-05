package com.gm.dao;

import com.gm.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class OrderDaoTest {

    @Resource
    private OrderDao orderDao;

    @Test
    public void insertOrder() throws Exception {

        long productId=1003L;
        long userPhone=15851835120L;
        int insertCount= orderDao.insertOrder(productId,userPhone);
        System.out.println("insertCount="+insertCount);
    }

    @Test
    public void queryByProductId() throws Exception {
        long productId=1003L;
        long userPhone=13476191877L;
        Order order = orderDao.queryByProductId(productId,userPhone);
        System.out.println(order);
        System.out.println(order.getProduct());


    }

}