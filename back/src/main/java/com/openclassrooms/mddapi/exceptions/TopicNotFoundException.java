package com.openclassrooms.mddapi.exceptions;

import org.springframework.http.HttpStatus;

public class TopicNotFoundException extends ApiException {

  public TopicNotFoundException(Integer topicId) {
    super(HttpStatus.NOT_FOUND, "Topic not found");
  }
}
