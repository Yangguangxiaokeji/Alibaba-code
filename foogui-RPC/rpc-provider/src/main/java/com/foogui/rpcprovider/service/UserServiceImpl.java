package com.foogui.rpcprovider.service;

import com.foogui.rpcapi.dto.UserDTO;
import com.foogui.rpcapi.service.RemoteUserService;
import com.foogui.rpcprovider.annotation.RpcService;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RpcService
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
