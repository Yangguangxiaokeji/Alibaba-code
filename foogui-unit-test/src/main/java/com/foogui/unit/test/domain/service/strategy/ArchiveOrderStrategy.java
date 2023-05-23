package com.foogui.unit.test.domain.service.strategy;

import com.foogui.unit.test.domain.entity.Order;
import com.foogui.unit.test.domain.enums.OrderStatus;
import com.foogui.unit.test.exception.BadRequestException;

public class ArchiveOrderStrategy implements OrderStrategy {
    @Override
    public boolean match(Order order) {
        return order.getStatus() == OrderStatus.ARCHIVE;
    }

    @Override
    public void archive(Order order) {
        throw new BadRequestException("Archive order failed, order has been archived");
    }
}