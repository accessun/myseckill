package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {

    /**
     * 插入购买明细, 可过滤重复(忽略重复秒杀, 不写入数据库)
     *
     * @param seckillId
     *            参与秒杀活动的商品的ID
     * @param userPhone
     *            用户手机号
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据ID查询SuccessKilled并携带秒杀商品对象
     *
     * @param seckillId
     *            参与秒杀活动的商品的ID
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
