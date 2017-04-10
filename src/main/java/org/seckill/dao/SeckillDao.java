package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {

    /**
     * 减库存, 带时间的目的是, SQL语句判断是否在秒杀活动时间内
     *
     * @param seckillId
     *            参与秒杀活动的商品的ID
     * @param killTime
     *            下单时间
     * @return 如果影响行数>1, 表示更新记录的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据ID查询秒杀商品
     *
     * @param seckillId
     *            参与秒杀活动的商品的ID
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀逻辑
     *
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);

}
