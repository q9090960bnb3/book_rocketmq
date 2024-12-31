package com.q9090960bnb3.mapper;

import com.q9090960bnb3.domain.Goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> selectSeckillGoods();

    int updateStock(Integer goodsId);
}