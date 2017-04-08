package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

public class SeckillServiceImpl implements SeckillService {
    private final Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    private final String salt = "JzX>VcRJt9xil]A(U]qf1yYlBZkOlC";

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposeSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null)
            return new Exposer(false, seckillId);

        long startTime = seckill.getStartTime().getTime();
        long endTime = seckill.getEndTime().getTime();
        long currentTime = new Date().getTime();

        if (currentTime < startTime || currentTime > endTime)
            return new Exposer(false, seckillId, currentTime, startTime, endTime);

        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("Seckill URL has been tampered with");
        }

        try {
            // 1. 减库存
            Date killTime = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId, killTime);
            if (updateCount <= 0) {
                // 不在活动时间内或是库存没了
                throw new SeckillCloseException("Seckill closed");
            } else {
                // 2. 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("Seckill repeated");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译时异常转化为运行时异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    private String getMd5(long seckillId) {
        String base = "~~" + seckillId + "~~" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}