package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(SeckillServiceTest.class);

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void testGetById() {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void testSeckillLogic() {
        long seckillId = 1001;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            long userPhone = 13721885736L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀活动未开始或者过期
            logger.warn("exposer={}", exposer);
        }
    }
    /*
    @Test
    public void testExposeSeckillUrl() {
        long seckillId = 1000;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        logger.info("exposer={}", exposer);
    }

    @Test
    public void testExecuteSeckill() {
        long seckillId = 1000;
        long userPhone = 13721885736L;
        String md5 = "4c0d3266b9e8a9acba93c182076d95df";
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result={}", execution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
    }
    */

    @Test
    public void testExecuteSeckillProcedure() {
        long seckillId = 1001;
        long phone = 13828542879L;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);

        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }

    }
}
