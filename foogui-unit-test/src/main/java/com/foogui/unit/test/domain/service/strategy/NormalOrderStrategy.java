package com.foogui.unit.test.domain.service.strategy;

import com.foogui.unit.test.domain.entity.Order;
import com.foogui.unit.test.domain.enums.OrderStatus;

public class NormalOrderStrategy implements OrderStrategy{
    @Override
    public boolean match(Order order) {
        return order.getStatus() == OrderStatus.NORMAL;
    }

    @Override
    public void archive(Order order) {
        order.effective();
        order.archive();
    }
}