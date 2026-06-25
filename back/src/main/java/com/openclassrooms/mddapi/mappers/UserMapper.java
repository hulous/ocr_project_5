package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.responses.UserResponse;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserResponse toResponse(User user) {
    if (user == null) {
      return null;
    }

    return new UserResponse()
      .setId(user.getId())
      .setName(user.getName())
      .setEmail(user.getEmail())
      .setCreatedAt(user.getCreatedAt())
      .setUpdatedAt(user.getUpdatedAt());
  }
}
