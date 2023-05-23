package com.foogui.unit.test.interactive.controller;

import com.foogui.unit.test.domain.entity.Order;
import com.foogui.unit.test.interactive.request.OrderCreateRequest;
import com.foogui.unit.test.interactive.response.ResponseWrapper;
import com.foogui.unit.test.interactive.validator.OrderRequestValidator;
import com.foogui.unit.test.service.OrderApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    private final OrderRequestValidator orderRequestValidator;

    @Autowired
    public OrderController(OrderApplicationService orderApplicationService, OrderRequestValidator orderRequestValidator) {
        this.orderApplicationService = orderApplicationService;
        this.orderRequestValidator = orderRequestValidator;
    }

    @PostMapping("/orders")
    public ResponseWrapper create(@RequestBody @Valid @NotNull OrderCreateRequest orderCreateRequest) {
        orderRequestValidator.validateCreate(orderCreateRequest);
        Order order = orderCreateRequest.convert();
        orderApplicationService.create(order);
        return ResponseWrapper.success();
    }
}