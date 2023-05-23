package com.foogui.unit.test.service;

import com.foogui.unit.test.adapter.cache.UserCacheAdapter;
import com.foogui.unit.test.adapter.cache.model.UserCacheDTO;
import com.foogui.unit.test.adapter.integration.UserAdapter;
import com.foogui.unit.test.adapter.integration.model.UserDTO;
import com.foogui.unit.test.domain.entity.Order;
import com.foogui.unit.test.domain.repository.OrderRepository;
import com.foogui.unit.test.domain.valueObject.User;
import org.springframework.stereotype.Service;

@Service
public class OrderApplicationService {
    private final UserCacheAdapter userCacheAdapter;
    private final UserAdapter userAdapter;
    private final OrderRepository orderRepository;

    public OrderApplicationService(UserCacheAdapter userCacheAdapter, UserAdapter userAdapter, OrderRepository orderRepository) {
        this.userCacheAdapter = userCacheAdapter;
        this.userAdapter = userAdapter;
        this.orderRepository = orderRepository;
    }

    public void create(Order order) {
        User member = order.getMember();
        Long memberId = member.getId();
        UserCacheDTO userCacheDTO = userCacheAdapter.getUserById(memberId);
        if (userCacheDTO != null) {
            member.setName(userCacheDTO.getName());
        } else {
            this.getUserFromExternalSystem(member, memberId);
        }
        orderRepository.save(order);
    }

    private void getUserFromExternalSystem(User member, Long memberId) {
        UserDTO userDTO = userAdapter.getExternalUserInfo(memberId);
        if(userDTO != null) {
            member.setName(userDTO.getName());
            userCacheAdapter.cacheUser(userDTO);
        }
    }
}