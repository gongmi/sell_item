drop DATABASE  IF EXISTS  SecondKill  ;
-- 创建数据库
 CREATE DATABASE SecondKill;
-- 使用数据库
use SecondKill;
CREATE TABLE `product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`product_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- 初始化数据
INSERT into product(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iphone6(已结束)',100,'2016-01-01 00:00:00','2017-01-02 00:00:00'),
  ('800元秒杀ipad(正在进行)',200,'2016-01-01 00:00:00','2018-01-02 00:00:00'),
  ('1元秒杀墨尔本机票(正在进行)',300,'2016-01-01 00:00:00','2018-01-02 00:00:00'),
  ('7000元秒杀mac(未开始)',400,'2018-01-01 00:00:00','2019-01-02 00:00:00');

-- 秒杀成功后的订单表
-- 用户登录认证相关信息(简化为手机号)
CREATE TABLE `order`(
  `product_id` BIGINT NOT NULL COMMENT '秒杀商品ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT 0 COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(product_id,user_phone),/*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';