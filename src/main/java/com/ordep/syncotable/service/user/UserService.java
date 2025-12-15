package com.ordep.syncotable.service.user;

import com.ordep.syncotable.dto.user.UserDto;
import com.ordep.syncotable.dto.user.request.UpdateUserRequest;
import com.ordep.syncotable.dto.user.response.UserResponse;
import com.ordep.syncotable.model.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto dto);
    User findUserByEmail(String email);
    List<UserResponse> findAllUsers();
    void updateUser(Long id, UpdateUserRequest update);
    void deleteUser(Long id);

}