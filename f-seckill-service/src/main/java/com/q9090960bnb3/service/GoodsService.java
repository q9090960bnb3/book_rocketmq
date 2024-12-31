package com.q9090960bnb3.service;

import com.q9090960bnb3.domain.Goods;
public interface GoodsService{

    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    /**
     * 真正处理秒杀业务
     * @param userId
     * @param goodsId
     */
    void realSeckill(Integer userId, Integer goodsId);

}
