package com.openclassrooms.mddapi.testdata;

import com.openclassrooms.mddapi.dtos.LoginUserDto;
import com.openclassrooms.mddapi.dtos.RegisterUserDto;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.responses.UserResponse;

import java.util.Date;

public final class UserTestData {

  private UserTestData() {
  }

  public static RegisterUserDto registerUserDto(String email, String username, String password) {
    return new RegisterUserDto().setEmail(email).setUsername(username).setPassword(password);
  }

  public static LoginUserDto loginUserDto(String email, String password) {
    return new LoginUserDto().setEmail(email).setPassword(password);
  }

  public static User user(int id, String username, String email, String password) {
    return new User()
      .setId(id)
      .setUsername(username)
      .setEmail(email)
      .setPassword(password);
  }

  public static UserResponse userResponse(int id, String username, String email, Date createdAt, Date updatedAt) {
    return new UserResponse()
      .setId(id)
      .setUsername(username)
      .setEmail(email)
      .setCreatedAt(createdAt)
      .setUpdatedAt(updatedAt);
  }
}
