package com.doubleA.UniTrade.service.user;

import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.repository.UserRepository;
import com.doubleA.UniTrade.request.CreateUserRequest;
import com.doubleA.UniTrade.request.UpdateUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// To be used as implementation class of IUserService.
@Service
// For dependency injection of final fields.
@RequiredArgsConstructor
public class UserService implements IUserService {
  private final UserRepository userRepository;

  @Override
  public User createUser(CreateUserRequest request) {
    return Optional.of(request)
        .filter(createUserRequest -> !userRepository.existsByEmail(request.getEmail()))
        .map(
            createUserRequest -> {
              User newUser = new User();
              newUser.setFirstName(request.getFirstName());
              newUser.setLastName(request.getLastName());
              newUser.setEmail(request.getEmail());
              newUser.setPassword(request.getPassword());
              return userRepository.save(newUser);
            })
        .orElseThrow(() -> new EntityExistsException(request.getEmail() + "already exists."));
  }

  @Override
  public User updateUser(UpdateUserRequest request, Long userId) {
    return userRepository
        .findById(userId)
        .map(
            existingUser -> {
              existingUser.setFirstName(request.getFirstName());
              existingUser.setLastName(request.getLastName());
              return userRepository.save(existingUser);
            })
        .orElseThrow(() -> new EntityNotFoundException("User not found."));
  }

  @Override
  public User getUserById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found."));
  }

  @Override
  public void deleteUser(Long userId) {
    userRepository
        .findById(userId)
        .ifPresentOrElse(
            userRepository::delete,
            () -> {
              throw new EntityNotFoundException("User not found.");
            });
  }
}
