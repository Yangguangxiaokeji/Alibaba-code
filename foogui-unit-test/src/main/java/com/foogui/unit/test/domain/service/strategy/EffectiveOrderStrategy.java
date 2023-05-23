package com.foogui.unit.test.domain.service.strategy;

import com.foogui.unit.test.domain.entity.Order;
import com.foogui.unit.test.domain.enums.OrderStatus;

public class EffectiveOrderStrategy implements OrderStrategy {
    @Override
    public boolean match(Order order) {
        return order.getStatus() == OrderStatus.EFFECTIVE;
    }

    @Override
    public void archive(Order order) {
        order.archive();
    }
}