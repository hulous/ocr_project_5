package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.ApiException;
import com.openclassrooms.mddapi.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.responses.UserResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceTest {

  private UserRepository userRepository;
  private UserMapper userMapper;
  private UserService service;

  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userMapper = Mockito.mock(UserMapper.class);
    service = new UserService(userRepository, userMapper);
  }

  @Test
  void showReturnsMappedResponseWhenUserExists() {
    User user = new User().setId(1).setEmail("test@example.com").setUsername("test");
    UserResponse response = new UserResponse().setId(1).setUsername("test").setEmail("test@example.com");

    when(userRepository.findById(1)).thenReturn(Optional.of(user));
    when(userMapper.toResponse(user)).thenReturn(response);

    UserResponse result = service.show(1);

    assertEquals(response, result);
  }

  @Test
  void showThrowsNotFoundWhenUserMissing() {
    when(userRepository.findById(2)).thenReturn(Optional.empty());

    ApiException exception = assertThrows(ApiException.class, () -> service.show(2));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    assertEquals("User not found", exception.getMessage());
  }
}
