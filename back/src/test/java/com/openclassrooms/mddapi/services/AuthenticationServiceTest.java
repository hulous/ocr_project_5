package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.LoginUserDto;
import com.openclassrooms.mddapi.dtos.RegisterUserDto;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.testdata.UserTestData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthenticationService service;

  @Test
  void registrateThrowsWhenEmailAlreadyExists() {
    RegisterUserDto dto = UserTestData.registerUserDto("john@example.com", null, null);
    when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registrate(dto));

    assertEquals("A user with this email already exists", exception.getMessage());
  }

  @Test
  void registrateSavesEncodedPassword() {
    RegisterUserDto dto = UserTestData.registerUserDto("john@example.com", "John", "raw");
    when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
    when(passwordEncoder.encode("raw")).thenReturn("encoded");

    User saved = UserTestData.user(1, "John", "john@example.com", "encoded");
    when(userRepository.save(any(User.class))).thenReturn(saved);

    User result = service.registrate(dto);

    assertEquals(1, result.getId());
    assertEquals("encoded", result.getPassword());
  }

  @Test
  void authenticateReturnsUserWhenCredentialsAreValid() {
    LoginUserDto dto = UserTestData.loginUserDto("john@example.com", "pwd");
    User user = UserTestData.user(0, "", "john@example.com", null);

    when(userRepository.findFirstByEmailOrUsername("john@example.com", "john@example.com")).thenReturn(Optional.of(user));

    User result = service.authenticate(dto);

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    assertEquals("john@example.com", result.getEmail());
  }

  @Test
  void authenticateReturnsUserWhenUsernameCredentialsAreValid() {
    LoginUserDto dto = UserTestData.loginUserDto("john", "pwd");
    User user = UserTestData.user(0, "john", "john@example.com", null);

    when(userRepository.findFirstByEmailOrUsername("john", "john")).thenReturn(Optional.of(user));

    User result = service.authenticate(dto);

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    assertEquals("john@example.com", result.getEmail());
  }

  @Test
  void authenticateThrowsWhenUserCannotBeFoundAfterAuth() {
    LoginUserDto dto = new LoginUserDto().setEmail("john@example.com").setPassword("pwd");
    when(userRepository.findFirstByEmailOrUsername("john@example.com", "john@example.com")).thenReturn(Optional.empty());

    UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> service.authenticate(dto));

    assertEquals("Invalid credentials", exception.getMessage());
  }
}
