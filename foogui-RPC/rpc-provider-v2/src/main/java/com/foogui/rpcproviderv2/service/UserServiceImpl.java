package com.foogui.rpcproviderv2.service;

import com.foogui.rpcapi.dto.UserDTO;
import com.foogui.rpcapi.service.RemoteUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserServiceImpl implements RemoteUserService {
    private final Map<String,Object> CACHE =new ConcurrentHashMap<>();

    @PostConstruct
    private void initCache() {
        UserDTO user01 = new UserDTO(1,"富贵1号");
        UserDTO user02 = new UserDTO(2,"富贵2号");
        CACHE.put(user01.getId().toString(), user01);
        CACHE.put(user02.getId().toString(), user02);
    }

    @Override
    public UserDTO getById(Integer id) {
        return (UserDTO)CACHE.get(id+"");
    }
}
