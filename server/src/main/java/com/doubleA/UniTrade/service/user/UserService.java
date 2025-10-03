package com.doubleA.UniTrade.service.user;

import com.doubleA.UniTrade.dtos.UserDto;
import com.doubleA.UniTrade.model.Role;
import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.repository.AddressRepository;
import com.doubleA.UniTrade.repository.RoleRepository;
import com.doubleA.UniTrade.repository.UserRepository;
import com.doubleA.UniTrade.request.CreateUserRequest;
import com.doubleA.UniTrade.request.UpdateUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

// To be used as implementation class of IUserService.
@Service
// For dependency injection of final fields.
@RequiredArgsConstructor
public class UserService implements IUserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final AddressRepository addressRepository;
  private final RoleRepository roleRepository;

  @Override
  public User createUser(CreateUserRequest request) {
    Role userRole =
        Optional.ofNullable(roleRepository.findByName("ROLE_USER"))
            .orElseThrow(() -> new EntityNotFoundException("Role Not Found"));

    return Optional.of(request)
        .filter(createUserRequest -> !userRepository.existsByEmail(request.getEmail()))
        .map(
            createUserRequest -> {
              User newUser = new User();
              newUser.setFirstName(request.getFirstName());
              newUser.setLastName(request.getLastName());
              newUser.setEmail(request.getEmail());
              newUser.setPassword(passwordEncoder.encode(request.getPassword()));
              newUser.setRoles(Set.of(userRole));
              User savedUser = userRepository.save(newUser);

              Optional.ofNullable(request.getAddressList())
                  .ifPresent(
                      addressList -> {
                        addressList.forEach(
                            address -> {
                              address.setUser(savedUser);
                              addressRepository.save(address);
                            });
                      });
              return savedUser;
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

  @Override
  public UserDto convertToDto(User user) {

    return modelMapper.map(user, UserDto.class);
  }

  // Block unauthenticated user from accessing.
  @Override
  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return Optional.ofNullable(userRepository.findByEmail(email))
        .orElseThrow(() -> new EntityNotFoundException("Please login first."));
  }
}
