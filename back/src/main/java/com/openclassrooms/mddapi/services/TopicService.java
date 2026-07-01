package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.dtos.TopicDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.ApiException;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.mappers.TopicMapper;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;
  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final CurrentUserService currentUserService;

  public TopicService(
    TopicRepository topicRepository,
    TopicMapper topicMapper,
    PostRepository postRepository,
    PostMapper postMapper,
    CurrentUserService currentUserService
  ) {
    this.topicRepository = topicRepository;
    this.topicMapper = topicMapper;
    this.postRepository = postRepository;
    this.postMapper = postMapper;
    this.currentUserService = currentUserService;
  }

  public List<TopicDto> listTopics() {
    return topicRepository
      .findAll()
      .stream()
      .map(topicMapper::toDto)
      .collect(Collectors.toList());
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
