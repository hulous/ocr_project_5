package com.openclassrooms.mddapi.exceptions;

import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExistsException extends ApiException {

  public SubscriptionAlreadyExistsException(Integer topicId) {
    super(HttpStatus.BAD_REQUEST, "Already subscribed to this topic");
  }
}
