package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.TopicResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

  @Mock
  private TopicRepository topicRepository;

  @Mock
  private CurrentUserService currentUserService;

  @InjectMocks
  private TopicService service;

  @Test
  void shouldMarkTopicAsSubscribedWhenCurrentUserIsSubscribed() {
    User currentUser = new User().setId(20);
    TopicResponse response = new TopicResponse().setId(5).setTitle("Java").setSubscribed(true);

    given(currentUserService.getCurrentUserOrNull()).willReturn(currentUser);
    given(topicRepository.findAllWithSubscribedFlagByUser(currentUser)).willReturn(List.of(response));

    List<TopicResponse> results = service.listTopics();

    assertEquals(1, results.size());
    assertTrue(results.get(0).isSubscribed());
  }

  @Test
  void shouldNotMarkTopicAsSubscribedWhenNoUserIsAuthenticated() {
    TopicResponse response = new TopicResponse().setId(6).setTitle("Spring").setSubscribed(false);

    given(currentUserService.getCurrentUserOrNull()).willReturn(null);
    given(topicRepository.findAllWithSubscribedFlagByUser(null)).willReturn(List.of(response));

    List<TopicResponse> results = service.listTopics();

    assertEquals(1, results.size());
    assertFalse(results.get(0).isSubscribed());
  }
}
