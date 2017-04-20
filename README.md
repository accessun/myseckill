# 一个简单的商品秒杀系统的实现

本项目为电子商务秒杀系统的简单实现.

项目代码仅以学习目的使用. 原教程请参见[慕课网Java秒杀系统的实现](http://www.imooc.com/u/2145618/courses?sort=publish)

## 技术栈:

- Spring: 依赖注入和事务管理
- Spring MVC: MVC网络框架
- MyBatis: ORM框架, 负责对象关系映射以及调用数据库操作
- MySQL: 用于保存库存信息和订单信息
- Redis: 缓存热点数据
- [Protostuff](http://www.protostuff.io/): 序列化
- JSP, JSTL: 网页模板和动态标签
- jQuery: DOM及AJAX操作
- Bootstrap: CSS和弹出框组件

## 简述

- 何为秒杀(seckill)系统?
  秒杀原是游戏中的词汇, 其本意为在极短的时间内杀掉敌人. 现延伸义为, 在极短的时间售出大量的商品, 通常这种活动会出现在电子商务网站上(参考: [Urban Dictionary](http://www.urbandictionary.com/define.php?term=seckill)). 卖家贴出秒杀商品, 在未来特定的时间点, 开放给注册用户下单. 这样的商品一般折扣力度大, 会吸引众多的购买者. 大量的购买者会在开放出售的一瞬间同时下单, 服务器在这一时刻要接收高聚集度的HTTP请求. 因此, 秒杀系统的构建本质上在于如何合理地处理这种高并发的请求量, 以确保系统的稳定性、安全性、能够快速响应请求等关键要求

- 秒杀系统所涉及的关键问题
  1. 如何防止重复请求
  2. 如何防止提早下单
  3. 如何合理地处理事务
  4. 如何有效降低活动开始时服务器的压力