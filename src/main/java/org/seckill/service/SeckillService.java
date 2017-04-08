package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

public interface SeckillService {

    /**
     * 查询所有参与秒杀活动的商品的库存信息
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询某个具体的参与秒杀活动的商品的库存信息
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * [重点]秒杀活动开启时, 输出秒杀商品的URL; 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exposeSeckillUrl(long seckillId);

    /**
     * 执行秒杀(下单)操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

}
