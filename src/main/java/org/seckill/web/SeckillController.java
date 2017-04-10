package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.put("list", list);
        return "list";
    }

    /*-
     * 某个参与秒杀活动的商品的详情信息页, 此处商品信息是直接从数据库中取出的, 由于这是个热点页(活动即将开始时, 大量用户可能会不停地刷新),
     * 因此需要做出一定的措施. 一般来说, 这个页面和上面的列表页会做成静态页面放到CDN上以减轻web服务器的压力, 其中涉及的动态的业务数据的
     * 获取做成AJAX即可.
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, ModelMap model) {
        if (seckillId == null)
            return "redirect:/seckill/list";
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null)
            return "forward:/seckill/list"; // for demonstrative purpose, redirection recommended
        model.put("seckill", seckill);
        return "detail";

    }

    /*-
     * 为何做一个秒杀下单地址的暴露接口呢? 因为我们不希望用户在秒杀活动开始之前就猜出下单的地址是什么, 如果被用户猜出来, 整个活动就毁了.
     * 所以, 这个下单的地址绝对不能是可预测的, 一方面来说绝对不可以对所有用户都是一样的. 那么, 既然下单地址对每个用户都不同, 很自然地
     * 就会想到, 我们用用户的唯一标识符(手机号或是email地址或是身份证号)就好了, 把标识符作为下单地址的一部分, 这样别的用户不会猜测出
     * 其他用户的地址了, 但是对于用户自身而言, ta还是有可能猜出自己的地址从而提前下单, 这也是我们不希望出现的. 因此, 我们需要把用户的
     * 唯一标识符混入随机盐, 然后用一个摘要算法(MD5或SHA)得到一个定长的字符串, 在下单地址中混入这样的定长随机字符串, 可以完全解决这个
     * 问题. 接下来做的只是在秒杀活动开始之时把下单地址暴露给用户就好了.
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    /*
     * 执行秒杀下单, 此处对应的业务逻辑是需要重点优化的
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5, @CookieValue(name = "userPhone", required = false) Long userPhone) {

        if (userPhone == null) {
            return new SeckillResult<>(false, "请先登录");
        }
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    /*-
     * 单独做一个AJAX请求获取系统时间的原因:
     * detail页为了应对高并发减轻系统压力会作为静态资源放到CDN(Content Delivery Network)上,
     * 这时获取系统时间只能单独拿出来, 做成这样的AJAX请求.
     *
     * 由于此方法并未涉及任何网络调用, 仅是JVM内存操作, 耗时在纳秒量级, 无需为高并发做特殊处理
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getTime() {
        return new SeckillResult<Long>(true, new Date().getTime());
    }

}
