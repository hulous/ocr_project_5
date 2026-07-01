package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.services.AuthenticationService;
import com.openclassrooms.mddapi.dtos.LoginUserDto;
import com.openclassrooms.mddapi.dtos.RegisterUserDto;
import com.openclassrooms.mddapi.responses.LoginResponse;
import com.openclassrooms.mddapi.responses.UserResponse;
import com.openclassrooms.mddapi.testdata.UserTestData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationsControllerTest {

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private AuthenticationsController controller;

  @Test
  void registrateReturnsCreatedUser() {
    RegisterUserDto dto = UserTestData.registerUserDto("john@example.com", "John", "pwd");
    UserResponse responseBody = UserTestData.userResponse(1, "John", "john@example.com", null, null);

    when(authenticationService.registrateResponse(dto)).thenReturn(responseBody);

    ResponseEntity<?> response = controller.registrate(dto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(responseBody, response.getBody());
  }

  @Test
  void registrateReturnsBadRequestOnDuplicateEmail() {
    RegisterUserDto dto = new RegisterUserDto().setEmail("john@example.com");
    when(authenticationService.registrateResponse(dto)).thenThrow(new IllegalArgumentException("A user with this email already exists"));

    assertThrows(IllegalArgumentException.class, () -> controller.registrate(dto));
  }

  @Test
  void registrateReturnsInternalServerErrorOnUnexpectedException() {
    RegisterUserDto dto = new RegisterUserDto().setEmail("john@example.com");
    when(authenticationService.registrateResponse(dto)).thenThrow(new RuntimeException("boom"));

    assertThrows(RuntimeException.class, () -> controller.registrate(dto));
  }

  @Test
  void authenticateReturnsLoginResponse() {
    LoginUserDto dto = UserTestData.loginUserDto("john@example.com", "pwd");
    LoginResponse loginResponse = new LoginResponse().setToken("jwt-token").setExpiresIn(3600L);

    when(authenticationService.authenticateResponse(dto)).thenReturn(loginResponse);

    ResponseEntity<LoginResponse> response = controller.authenticate(dto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    LoginResponse body = response.getBody();
    assertEquals("jwt-token", body.getToken());
    assertEquals(3600L, body.getExpiresIn());
  }

  @Test
  void authenticateReturnsUnauthorizedOnBadCredentials() {
    LoginUserDto dto = new LoginUserDto().setEmail("john@example.com").setPassword("bad");
    when(authenticationService.authenticateResponse(dto)).thenThrow(new BadCredentialsException("bad"));

    assertThrows(BadCredentialsException.class, () -> controller.authenticate(dto));
  }

  @Test
  void authenticateReturnsInternalServerErrorOnUnexpectedException() {
    LoginUserDto dto = new LoginUserDto().setEmail("john@example.com").setPassword("pwd");
    when(authenticationService.authenticateResponse(dto)).thenThrow(new RuntimeException("boom"));

    assertThrows(RuntimeException.class, () -> controller.authenticate(dto));
  }

  @Test
  void authenticatedUserReturnsCurrentPrincipal() {
    Date now = new Date();
    UserResponse responseBody = UserTestData.userResponse(99, "Alice", "alice@example.com", now, now);

    when(authenticationService.authenticatedUser()).thenReturn(responseBody);

    ResponseEntity<UserResponse> response = controller.authenticatedUser();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    UserResponse body = response.getBody();
    assertEquals(responseBody.getId(), body.getId());
    assertEquals(responseBody.getEmail(), body.getEmail());
  }

}
