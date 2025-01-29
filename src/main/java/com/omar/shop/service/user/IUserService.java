package com.omar.shop.service.user;

import com.omar.shop.model.User;
import com.omar.shop.request.CreateUserRequest;
import com.omar.shop.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);
}
