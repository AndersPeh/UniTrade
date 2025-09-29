package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.dtos.UserDto;
import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.request.CreateUserRequest;
import com.doubleA.UniTrade.request.UpdateUserRequest;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final IUserService userService;

  @GetMapping("/user/{userId}/user")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    UserDto userDto = userService.convertToDto(user);
    return ResponseEntity.ok(new ApiResponse("Success", userDto));
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
    User user = userService.createUser(request);
    UserDto userDto = userService.convertToDto(user);

    return ResponseEntity.ok(new ApiResponse("New User Creation Success", userDto));
  }

  @PutMapping("/{userId}/update")
  public ResponseEntity<ApiResponse> updateUser(
      @PathVariable Long userId, @RequestBody UpdateUserRequest request) {
    User user = userService.updateUser(request, userId);
    UserDto userDto = userService.convertToDto(user);

    return ResponseEntity.ok(new ApiResponse("User Update Success", userDto));
  }

  @DeleteMapping("/{userId}/delete")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(new ApiResponse("User Deletion Success", null));
  }
}
