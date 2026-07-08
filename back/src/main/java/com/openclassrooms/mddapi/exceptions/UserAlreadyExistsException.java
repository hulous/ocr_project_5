package com.openclassrooms.mddapi.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {

  public UserAlreadyExistsException(String email) {
    super(HttpStatus.CONFLICT, "user already exist");
  }
}
