package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.services.UserService;
import com.openclassrooms.mddapi.exceptions.ApiException;
import com.openclassrooms.mddapi.responses.UserResponse;
import com.openclassrooms.mddapi.testdata.UserTestData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UsersController controller;

  @Test
  void showReturnsUserWhenFound() {
    LocalDateTime now = LocalDateTime.now();
    UserResponse userResponse = UserTestData.userResponse(1, "John", "john@example.com", now, now);
    when(userService.show(1)).thenReturn(userResponse);

    ResponseEntity<UserResponse> response = controller.show(1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    UserResponse body = response.getBody();
    assertEquals(userResponse.getId(), body.getId());
    assertEquals(userResponse.getEmail(), body.getEmail());
  }

  @Test
  void showReturnsNotFoundWhenNotFound() {
    when(userService.show(10)).thenThrow(new ApiException(HttpStatus.NOT_FOUND, "User not found"));

    ApiException exception = assertThrows(ApiException.class, () -> controller.show(10));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    assertEquals("User not found", exception.getMessage());
  }

  @Test
  void showReturnsInternalServerErrorOnUnexpectedException() {
    when(userService.show(10)).thenThrow(new RuntimeException("boom"));

    assertThrows(RuntimeException.class, () -> controller.show(10));
  }
}
