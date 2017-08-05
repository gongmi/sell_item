package com.gm.dao.cache;

import com.gm.entity.Product;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by codingBoy on 17/2/17.
 */
public class RedisDao {
    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Product> schema = RuntimeSchema.createFrom(Product.class);


    public Product getSeckill(long seckillId) {
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "product:" + seckillId;
                //并没有实现哪部序列化操作
                //采用自定义序列化
                //protostuff: pojo.
                byte[] bytes = jedis.get(key.getBytes());
                //缓存重获取到
                if (bytes != null) {
                    Product product =schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, product,schema);
                    //seckill被反序列化

                    return product;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e) {

        }
        return null;
    }

    public String putSeckill(Product product) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "product:" + product.getProductId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(product, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;//1小时
                String result = jedis.setex(key.getBytes(),timeout,bytes);

                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e) {

        }

        return null;
    }
}
