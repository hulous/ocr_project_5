package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.PostNotFoundException;
import com.openclassrooms.mddapi.exceptions.TopicNotFoundException;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.PostDetailResponse;
import com.openclassrooms.mddapi.responses.PostResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final TopicRepository topicRepository;
  private final PostMapper postMapper;
  private final CommentService commentService;
  private final CurrentUserService currentUserService;

  public PostService(
    PostRepository postRepository,
    TopicRepository topicRepository,
    PostMapper postMapper,
    CommentService commentService,
    CurrentUserService currentUserService
  ) {
    this.postRepository = postRepository;
    this.topicRepository = topicRepository;
    this.postMapper = postMapper;
    this.commentService = commentService;
    this.currentUserService = currentUserService;
  }

  @Transactional(readOnly = true)
  public PostDetailResponse show(Integer postId) {
    Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new PostNotFoundException(postId));

    PostDetailResponse detail = postMapper.toDetailResponse(post);
    detail.setComments(commentService.listCommentsForPost(postId));

    return detail;
  }

  public PostResponse createPostForTopic(Integer topicId, CreatePostDto input) {
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

    return postMapper.toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<PostResponse> listPostsForTopic(Integer topicId) {
    topicRepository
      .findById(topicId)
      .orElseThrow(() -> new TopicNotFoundException(topicId));

    return postRepository
      .findAllByTopicId(topicId)
      .stream()
      .map(postMapper::toResponse)
      .collect(Collectors.toList());
  }
}
