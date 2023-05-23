package com.foogui.unit.test.data.dao;

import com.foogui.unit.test.data.model.OrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao {

    OrderDO selectOrderById(long id);

    void insert(OrderDO orderDO);
}