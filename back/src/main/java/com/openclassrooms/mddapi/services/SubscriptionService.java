package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entities.Subscription;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.NoSubscriptionFoundException;
import com.openclassrooms.mddapi.exceptions.SubscriptionAlreadyExistsException;
import com.openclassrooms.mddapi.exceptions.TopicNotFoundException;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  private final TopicRepository topicRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final CurrentUserService currentUserService;

  public SubscriptionService(
    TopicRepository topicRepository,
    SubscriptionRepository subscriptionRepository,
    CurrentUserService currentUserService
  ) {
    this.topicRepository = topicRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.currentUserService = currentUserService;
  }

  public ApiMessageResponse subscribeCurrentUser(Integer topicId) {
    Topic topic = topicRepository
      .findById(topicId)
      .orElseThrow(() -> new TopicNotFoundException(topicId));

    User currentUser = currentUserService.getCurrentUser();

    if (subscriptionRepository.existsByUserAndTopic(currentUser, topic)) {
      throw new SubscriptionAlreadyExistsException(topicId);
    }

    Subscription subscription = new Subscription()
      .setUser(currentUser)
      .setTopic(topic);

    subscriptionRepository.save(subscription);

    return new ApiMessageResponse().setMessage("Subscription created successfully");
  }

  public ApiMessageResponse unsubscribeCurrentUser(Integer topicId) {
    Topic topic = topicRepository
      .findById(topicId)
      .orElseThrow(() -> new TopicNotFoundException(topicId));

    User currentUser = currentUserService.getCurrentUser();

    Subscription subscription = subscriptionRepository
      .findByUserAndTopic(currentUser, topic)
      .orElseThrow(() -> new NoSubscriptionFoundException(topicId));

    subscriptionRepository.delete(subscription);

    return new ApiMessageResponse().setMessage("Subscription removed successfully");
  }
}
