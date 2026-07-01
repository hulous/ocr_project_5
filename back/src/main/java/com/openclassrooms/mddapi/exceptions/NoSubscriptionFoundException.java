package com.openclassrooms.mddapi.exceptions;

import org.springframework.http.HttpStatus;

public class NoSubscriptionFoundException extends ApiException {

  public NoSubscriptionFoundException(Integer topicId) {
    super(HttpStatus.BAD_REQUEST, "No subscription found for this topic");
  }
}
