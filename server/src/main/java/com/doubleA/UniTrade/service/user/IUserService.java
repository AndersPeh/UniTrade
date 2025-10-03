package com.doubleA.UniTrade.service.user;

import com.doubleA.UniTrade.dtos.UserDto;
import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.request.CreateUserRequest;
import com.doubleA.UniTrade.request.UpdateUserRequest;

public interface IUserService {
  User createUser(CreateUserRequest request);

  User updateUser(UpdateUserRequest request, Long userId);

  User getUserById(Long userId);

  void deleteUser(Long userId);

  UserDto convertToDto(User user);

  // Block unauthenticated user from accessing.
  User getAuthenticatedUser();
}
