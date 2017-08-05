package com.gm.dto;

import com.gm.entity.Order;
import com.gm.enums.StateEnum;

/**
 * 封装执行秒杀后的记录:是否秒杀成功
 */
public class KillRecord {

    private long productId;
    private int state;
    private String stateInfo;
    private Order order;

    //秒杀成功返回所有信息 的 constructor
    public KillRecord(long productId, StateEnum statEnum, Order order) {
        this.productId = productId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getInfo();
        this.order = order;
    }

    //秒杀失败constructor
    public KillRecord(long productId, StateEnum statEnum) {
        this.productId = productId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getInfo();
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {

        this.order = order;
    }

    @Override
    public String toString() {
        return "KillRecord{" +
                "productId=" + productId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", order=" + order +
                '}';
    }
}
