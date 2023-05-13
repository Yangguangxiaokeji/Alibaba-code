package com.foogui.rpcapi.service;

import com.foogui.rpcapi.dto.UserDTO;

public interface RemoteUserService {
    UserDTO getById(Integer id);
}
