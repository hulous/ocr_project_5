package com.openclassrooms.mddapi.exceptions;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ApiException {

  public PostNotFoundException(Integer postId) {
    super(HttpStatus.NOT_FOUND, "Post not found");
  }
}
