package com.foogui.unit.test.interactive.validator;

import com.foogui.unit.test.exception.BadRequestException;
import com.foogui.unit.test.interactive.request.OrderCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestValidator {

    public void validateCreate(OrderCreateRequest orderCreateRequest) {
        if (orderCreateRequest.getOrderAmountTotal().compareTo(orderCreateRequest.getProductAmountTotal()) > 0) {
            throw new BadRequestException("订单总价不能高于商品总价");
        }
    }
}