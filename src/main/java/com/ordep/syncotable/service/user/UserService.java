package com.ordep.syncotable.service.user;

import com.ordep.syncotable.dto.user.UserDto;
import com.ordep.syncotable.model.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto dto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();

}