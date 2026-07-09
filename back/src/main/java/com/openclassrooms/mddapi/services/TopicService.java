package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.TopicResponse;
import com.openclassrooms.mddapi.services.CurrentUserService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

  private final TopicRepository topicRepository;
  private final CurrentUserService currentUserService;

  public TopicService(
    TopicRepository topicRepository,
    CurrentUserService currentUserService
  ) {
    this.topicRepository = topicRepository;
    this.currentUserService = currentUserService;
  }

  public List<TopicResponse> listTopics() {
    User currentUser = currentUserService.getCurrentUserOrNull();

    return topicRepository.findAllWithSubscribedFlagByUser(currentUser);
  }

}
