package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.PostNotFoundException;
import com.openclassrooms.mddapi.exceptions.TopicNotFoundException;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.services.CurrentUserService;

import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final TopicRepository topicRepository;
  private final PostMapper postMapper;
  private final CurrentUserService currentUserService;

  public PostService(
    PostRepository postRepository,
    TopicRepository topicRepository,
    PostMapper postMapper,
    CurrentUserService currentUserService
  ) {
    this.postRepository = postRepository;
    this.topicRepository = topicRepository;
    this.postMapper = postMapper;
    this.currentUserService = currentUserService;
  }

  public PostDto show(Integer postId) {
    Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new PostNotFoundException(postId));

    return postMapper.toDto(post);
  }

  public PostDto createPostForTopic(Integer topicId, CreatePostDto input) {
    Topic topic = topicRepository
      .findById(topicId)
      .orElseThrow(() -> new TopicNotFoundException(topicId));

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
      .orElseThrow(() -> new TopicNotFoundException(topicId));

    return postRepository
      .findAllByTopicId(topicId)
      .stream()
      .map(postMapper::toDto)
      .collect(Collectors.toList());
  }
}
