package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.dtos.TopicDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.Subscription;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.ApiException;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.mappers.TopicMapper;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;
  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final SubscriptionRepository subscriptionRepository;
  private final CurrentUserService currentUserService;

  public TopicService(
    TopicRepository topicRepository,
    TopicMapper topicMapper,
    PostRepository postRepository,
    PostMapper postMapper,
    SubscriptionRepository subscriptionRepository,
    CurrentUserService currentUserService
  ) {
    this.topicRepository = topicRepository;
    this.topicMapper = topicMapper;
    this.postRepository = postRepository;
    this.postMapper = postMapper;
    this.subscriptionRepository = subscriptionRepository;
    this.currentUserService = currentUserService;
  }

  public List<TopicDto> listTopics() {
    return topicRepository
      .findAll()
      .stream()
      .map(topicMapper::toDto)
      .collect(Collectors.toList());
  }

  public ApiMessageResponse subscribeCurrentUser(Integer topicId) {
    Topic topic = topicRepository
      .findById(topicId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Topic not found"));

    User currentUser = currentUserService.getCurrentUser();

    if (subscriptionRepository.existsByUserAndTopic(currentUser, topic)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Already subscribed to this topic");
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
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Topic not found"));

    User currentUser = currentUserService.getCurrentUser();

    Optional<Subscription> subscription = subscriptionRepository.findByUserAndTopic(currentUser, topic);
    if (subscription.isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "No subscription found for this topic");
    }

    subscriptionRepository.delete(subscription.get());

    return new ApiMessageResponse().setMessage("Subscription removed successfully");
  }

  public PostDto createPostForTopic(Integer topicId, CreatePostDto input) {
    Topic topic = topicRepository
      .findById(topicId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Topic not found"));

    User currentUser = currentUserService.getCurrentUser();

    Post post = new Post()
      .setTopic(topic)
      .setAuthor(currentUser)
      .setTitle(input.getTitle())
      .setContent(input.getContent());

    Post saved = postRepository.save(post);

    return postMapper.toDto(saved);
  }

  public List<PostDto> listPostsForTopic(Integer topicId) {
    topicRepository
      .findById(topicId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Topic not found"));

    return postRepository
      .findAllByTopicId(topicId)
      .stream()
      .map(postMapper::toDto)
      .collect(Collectors.toList());
  }
}
