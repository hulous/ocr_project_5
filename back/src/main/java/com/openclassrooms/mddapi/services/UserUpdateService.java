package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.UpdateUserDto;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.responses.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserUpdateService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final CurrentUserService currentUserService;
  private final PasswordEncoder passwordEncoder;

  public UserUpdateService(
    UserRepository userRepository,
    UserMapper userMapper,
    CurrentUserService currentUserService,
    PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.currentUserService = currentUserService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public UserResponse updateCurrentUser(UpdateUserDto input) {
    User currentUser = currentUserService.getCurrentUser();

    User user = userRepository
      .findById(currentUser.getId())
      .orElseThrow(() -> new UserNotFoundException(currentUser.getId()));

    if (input.getEmail() != null && !input.getEmail().isBlank()) {
      user.setEmail(input.getEmail());
    }

    if (input.getUsername() != null && !input.getUsername().isBlank()) {
      user.setUsername(input.getUsername());
    }

    if (input.getPassword() != null && !input.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(input.getPassword()));
    }

    User updated = userRepository.save(user);
    return userMapper.toResponse(updated);
  }
}
