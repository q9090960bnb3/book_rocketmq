package com.q9090960bnb3.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.q9090960bnb3.mapper.GoodsMapper;
import com.q9090960bnb3.mapper.OrderRecordsMapper;
import com.q9090960bnb3.domain.Goods;
import com.q9090960bnb3.domain.OrderRecords;
import com.q9090960bnb3.service.GoodsService;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderRecordsMapper orderMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Goods record) {
        return goodsMapper.insert(record);
    }

    @Override
    public int insertSelective(Goods record) {
        return goodsMapper.insertSelective(record);
    }

    @Override
    public Goods selectByPrimaryKey(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Goods record) {
        return goodsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Goods record) {
        return goodsMapper.updateByPrimaryKey(record);
    }

    /**
     * 扣库存表
     * 写订单表
     */
    /**
     * 对应方案2，但压力全给数据库了，不适合并发量大的场景
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer userId, Integer goodsId) {
        int i = goodsMapper.updateStock(goodsId);
        if (i > 0) {
            OrderRecords order = new OrderRecords();
            order.setGoodsId(goodsId);
            order.setUserId(userId);
            order.setCreateTime(new Date());
            orderMapper.insert(order);
        }
    }

    /**
     * 这样也会失败，因为先开的事务再开的锁，mysql为可重复读，所以一样有并发问题
     */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public void realSeckill(Integer userId, Integer goodsId) {

    //     // GoodsServiceImpl service IOC 是单例对象 ，可以用this
    //     synchronized (this) {
    //         Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
    //         int finalstock = goods.getStocks() - 1;
    //         if (finalstock < 0) {
    //             throw new RuntimeException("商品" + goodsId + "库存不足,用户id为:" + userId);
    //         }
    //         goods.setStocks(finalstock);
    //         goods.setUpdateTime(new Date());
    //         int i = goodsMapper.updateByPrimaryKey(goods);
    //         if (i > 0) {
    //             OrderRecords order = new OrderRecords();
    //             order.setGoodsId(goodsId);
    //             order.setUserId(userId);
    //             order.setCreateTime(new Date());
    //             orderMapper.insert(order);
    //         }
    //     }

    // }

    /** 有并发问题，外部调用 synchronized 同步锁可以避免 */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public void realSeckill(Integer userId, Integer goodsId) {
    //     Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
    //     int finalstock = goods.getStocks() - 1;
    //     if (finalstock < 0) {
    //         throw new RuntimeException("商品" + goodsId + "库存不足,用户id为:" + userId);
    //     }
    //     goods.setStocks(finalstock);
    //     goods.setUpdateTime(new Date());
    //     int i = goodsMapper.updateByPrimaryKey(goods);
    //     if (i > 0) {
    //         OrderRecords order = new OrderRecords();
    //         order.setGoodsId(goodsId);
    //         order.setUserId(userId);
    //         order.setCreateTime(new Date());
    //         orderMapper.insert(order);
    //     }
    // }

}
