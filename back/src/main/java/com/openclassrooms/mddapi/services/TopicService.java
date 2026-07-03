package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.TopicResponse;
import com.openclassrooms.mddapi.mappers.TopicMapper;
import com.openclassrooms.mddapi.services.CurrentUserService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;
  private final SubscriptionRepository subscriptionRepository;
  private final CurrentUserService currentUserService;

  public TopicService(
    TopicRepository topicRepository,
    TopicMapper topicMapper,
    SubscriptionRepository subscriptionRepository,
    CurrentUserService currentUserService
  ) {
    this.topicRepository = topicRepository;
    this.topicMapper = topicMapper;
    this.subscriptionRepository = subscriptionRepository;
    this.currentUserService = currentUserService;
  }

  public List<TopicResponse> listTopics() {
    User currentUser = currentUserService.getCurrentUserOrNull();

    return topicRepository
      .findAll()
      .stream()
      .map(topic -> {
        TopicResponse response = topicMapper.toDto(topic);
        response.setSubscribed(
          currentUser != null && subscriptionRepository.existsByUserAndTopic(currentUser, topic)
        );
        return response;
      })
      .collect(Collectors.toList());
  }

}
