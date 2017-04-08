package org.seckill.dto;

/*
 * 暴露秒杀商品URL的DTO, 非业务直接相关, 仅是用于服务业务逻辑的数据的封装与传递
 */
public class Exposer {

    // 是否可以暴露URL
    private boolean exposed;
    private String md5;
    private long seckillId;
    // 系统当前时间(单位均是毫秒)
    private long currentTime;
    // 秒杀开启时间
    private long startTime;
    // 秒杀结束时间
    private long endTime;

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId, long currentTime, long startTime, long endTime) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.currentTime = currentTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
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

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
