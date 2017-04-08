package org.seckill.dto;

import org.seckill.entity.SuccessKilled;

/*
 * 封装秒杀执行之后的结果
 *
 */
public class SeckillExecution {

    private long seckillId;
    // 秒杀结果执行的状态
    private int state;
    // 状态的标识
    private String stateInfo;
    // 包含一个秒杀成功的下单记录
    private SuccessKilled successKilled;

    // 用于构造成功的秒杀执行结果
    public SeckillExecution(long seckillId, int state, String stateInfo, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
        this.successKilled = successKilled;
    }

    // 用于构造失败的秒杀执行结果
    public SeckillExecution(long seckillId, int state, String stateInfo) {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
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

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

}
