package com.openclassrooms.mddapi.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {

  public UserNotFoundException(Integer userId) {
    super(HttpStatus.NOT_FOUND, "User not found");
  }
}
