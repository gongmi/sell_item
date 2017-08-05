package com.gm.dto;

/**
 * 暴露秒杀地址(接口)DTO
 */
//    用于service
public class Exposer {

    private boolean exposed;
    private String md5;
    private long productId;
    private long now;
    private long start;
    private long end;

    //秒杀开启的constructor
    public Exposer(boolean exposed, String md5, long productId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.productId = productId;
    }

    //此product 秒杀未开启或者已结束的product的constructor
    public Exposer(boolean exposed, long productId, long now, long start, long end) {
        this.exposed = exposed;
        this.productId = productId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    //找不到此id 的product的constructor
    public Exposer(boolean exposed, long productId) {
        this.exposed = exposed;
        this.productId = productId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", productId=" + productId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
