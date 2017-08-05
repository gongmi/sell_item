package com.gm.service;

import com.gm.dto.Exposer;
import com.gm.dto.KillRecord;
import com.gm.entity.Product;
import com.gm.exception.KillException;
import com.gm.exception.RepeatKillException;
import com.gm.exception.KillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by codingBoy on 16/11/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})

public class ProductServiceTest {

    @Autowired
    private KillService killService;

    @Test
    public void getProductList() throws Exception {
        List<Product> products = killService.getProductList();
        System.out.println(products);
    }

    @Test
    public void getById() throws Exception {
        long productId = 1000;
        Product product = killService.getById(productId);
        System.out.println(product);
    }

    @Test
    public void exportKillUrl() {
        long productId = 1002;
        Exposer exposer = killService.exportKillUrl(productId);
        System.out.println(exposer);
    }


    @Test
    public void kill() throws KillException {
        long productId = 1002;
        long userPhone = 13476191876L;
        String md5 = "97c1af50687c20e78ec601a1e09b2bd8";
        //若秒杀失败 应该try catch 这样junit才会成功
        try {
            KillRecord killRecord = killService.kill(productId, userPhone, md5);
            System.out.println(killRecord);
        } catch (KillException e) {
            e.printStackTrace();
        }
    }

    //上面2个一起测试，可重复执行
    public void testProductLogic() throws Exception {
        long productId = 1003;
        Exposer exposer = killService.exportKillUrl(productId);
        System.out.println(exposer);

        ////秒杀已开启
        if (exposer.isExposed()) {
            long userPhone = 13476191876L;
            String md5 = exposer.getMd5();
            //若秒杀失败 应该try catch 这样junit才会成功
            try {
                KillRecord killRecord = killService.kill(productId, userPhone, md5);
                System.out.println(killRecord);
            } catch (KillException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void executeProductProcedure() {
        long productId = 1000;
        long userPhone = 13476191876L;
        Exposer exposer = killService.exportKillUrl(productId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            KillRecord killRecord = killService.kill(productId, userPhone, md5);
            System.out.println(killRecord);
        }
    }
}