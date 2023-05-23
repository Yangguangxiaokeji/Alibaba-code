package com.foogui.unit.test.domain.service.strategy;

import com.foogui.unit.test.domain.entity.Order;

public interface OrderStrategy {

    boolean match(Order order);

    void archive(Order order);
}