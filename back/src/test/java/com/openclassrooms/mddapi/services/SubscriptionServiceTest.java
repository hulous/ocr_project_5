package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entities.Subscription;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock
  private TopicRepository topicRepository;

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private CurrentUserService currentUserService;

  @InjectMocks
  private SubscriptionService service;

  @Test
  void shouldSubscribeCurrentUserWhenTopicExistsAndSubscriptionDoesNotExist() {
    Integer topicId = 12;
    Topic topic = new Topic().setId(topicId);
    User currentUser = new User().setId(33);

    given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));
    given(currentUserService.getCurrentUser()).willReturn(currentUser);
    given(subscriptionRepository.existsByUserAndTopic(currentUser, topic)).willReturn(false);

    ApiMessageResponse result = service.subscribeCurrentUser(topicId);

    assertEquals("Subscription created successfully", result.getMessage());
    verify(subscriptionRepository).save(argThat(subscription ->
      subscription.getTopic() == topic &&
      subscription.getUser() == currentUser
    ));
  }

  @Test
  void shouldUnsubscribeCurrentUserWhenSubscriptionExists() {
    Integer topicId = 13;
    Topic topic = new Topic().setId(topicId);
    User currentUser = new User().setId(45);
    Subscription subscription = new Subscription().setId(55).setTopic(topic).setUser(currentUser);

    given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));
    given(currentUserService.getCurrentUser()).willReturn(currentUser);
    given(subscriptionRepository.findByUserAndTopic(currentUser, topic)).willReturn(Optional.of(subscription));

    ApiMessageResponse result = service.unsubscribeCurrentUser(topicId);

    assertEquals("Subscription removed successfully", result.getMessage());
    verify(subscriptionRepository).delete(subscription);
  }
}
