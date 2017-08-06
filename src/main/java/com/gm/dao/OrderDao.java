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
/*
首先把  前端的页面和js  css 放在cdn上 减少 用户不断地刷新页面所带来的服务器的压力
刷新的时候 只发送ajax获取服务器的系统时间 time/now
在js中判断系统时间 与商品秒杀开始时间
如果未开始 则倒计时
如果开始了  则请求秒杀地址 product/id/exposer
在请求秒杀地址时  也要判断一下时间 这个时候会取数据库里 这个product的秒杀开始时间
所以  为了减少服务器与数据库的往返时间
把这个product 的相关信息写在 redis中

如果服务器也判断 秒杀开始的话
那么就返回这个product 的md5
然后js中组成 执行秒杀的ajax发送的 API ：product/id/MD5/kill
在执行秒杀这个方法（这个方法被事务包裹的）时
要先尝试 update product的库存减一
如果成功 再往order中 insert 这条订单记录

但是 update 方法 是会给这条记录加行锁的 所以
当多人执行时  会在这里等待这个锁 并发量变低

所以先执行insert 操作 insert操作可以并发执行
insert时 可以过滤 重复秒杀的情况
因为insert的主键是 productid + phone

然后再执行update
可能update失败 比如说买完了
这个时候roll back
所以之前 insert操作也会撤销
就不会出现 插入了订单 但是其实已经卖完了的情况

但是这个事务是spring控制的事务
（因此 update 语句 获得了这条数据的锁之后 执行这条语句  返回结果给服务器
要网络延迟 +可能的GC 才会 commit 或rollback释放锁 ）

可以改成 mysql自己控制的事务 即存储过程（
这样是获得了这条数据的锁之后 commit/rollback释放锁 再网络延迟+GC ）
因此减少了行锁的持有时间
 */
}
