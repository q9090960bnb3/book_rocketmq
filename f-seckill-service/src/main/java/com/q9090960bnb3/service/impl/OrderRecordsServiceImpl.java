package com.q9090960bnb3.service.impl;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.q9090960bnb3.domain.OrderRecords;
import com.q9090960bnb3.mapper.OrderRecordsMapper;
import com.q9090960bnb3.service.OrderRecordsService;
@Service
public class OrderRecordsServiceImpl implements OrderRecordsService{

    @Autowired
    private OrderRecordsMapper orderRecordsMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return orderRecordsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OrderRecords record) {
        return orderRecordsMapper.insert(record);
    }

    @Override
    public int insertSelective(OrderRecords record) {
        return orderRecordsMapper.insertSelective(record);
    }

    @Override
    public OrderRecords selectByPrimaryKey(Integer id) {
        return orderRecordsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OrderRecords record) {
        return orderRecordsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OrderRecords record) {
        return orderRecordsMapper.updateByPrimaryKey(record);
    }

}
