package com.q9090960bnb3.service;

import com.q9090960bnb3.domain.OrderRecords;
public interface OrderRecordsService{

    int deleteByPrimaryKey(Integer id);

    int insert(OrderRecords record);

    int insertSelective(OrderRecords record);

    OrderRecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRecords record);

    int updateByPrimaryKey(OrderRecords record);

}
