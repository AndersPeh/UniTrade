package com.doubleA.UniTrade.data;

import com.doubleA.UniTrade.model.Role;
import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.repository.RoleRepository;
import com.doubleA.UniTrade.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
// By setting transactional, the entire DataInitialiser is considered as a single
// transaction.
// Without transactional, adminRole becomes transient state and cant be saved in
// createDefaultRoles.
// Transactional puts everything in DataInitialiser in persistent state so they can
// be saved.
public class DataInitialiser implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN", "ROLE_CUSTOMER");

    // create default roles followed by default admin users.
    createDefaultRoles(defaultRoles);

    createDefaultAdminIfNotExists();
  }

  // Create default roles that dont exist in the database for initialisation.
  private void createDefaultRoles(Set<String> roles) {
    roles.stream()
        .filter(role -> Optional.ofNullable(roleRepository.findByName(role)).isEmpty())
        .map(Role::new)
        .forEach(roleRepository::save);
  }

  // Admin role should exist before creating 3 default admin users.
  private void createDefaultAdminIfNotExists() {
    Role adminRole =
        Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"))
            .orElseThrow(() -> new EntityNotFoundException("Role Not Found"));
    for (int i = 1; i <= 3; i++) {
      String defaultEmail = "admin" + i + "@email.com";

      // if the admin + index number exists, skip to the next admin email.
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("Admin");
      user.setLastName("Shop User" + i);
      user.setEmail(defaultEmail);
      user.setPassword(passwordEncoder.encode("123456"));
      user.setRoles(Set.of(adminRole));
      userRepository.save(user);
    }
  }
}
