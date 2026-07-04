package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.responses.CommentResponse;
import com.openclassrooms.mddapi.dtos.CreateCommentDto;
import com.openclassrooms.mddapi.entities.Comment;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.PostNotFoundException;
import com.openclassrooms.mddapi.mappers.CommentMapper;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;
  private final CurrentUserService currentUserService;

  public CommentService(
      PostRepository postRepository,
      CommentRepository commentRepository,
      CommentMapper commentMapper,
      CurrentUserService currentUserService
  ) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
    this.currentUserService = currentUserService;
  }

  public CommentResponse createCommentForPost(Integer postId, CreateCommentDto input) {
    Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new PostNotFoundException(postId));

    User currentUser = currentUserService.getCurrentUser();

    Comment comment = new Comment()
      .setPost(post)
      .setAuthor(currentUser)
      .setContent(input.getContent());

    Comment savedComment = commentRepository.save(comment);
    CommentResponse response = commentMapper.toDto(savedComment);

    if (response.getAuthorUsername() == null && savedComment.getAuthor() != null) {
      response.setAuthorUsername(savedComment.getAuthor().getUsername());
    }

    return response;
  }

  public List<CommentResponse> listCommentsForPost(Integer postId) {
    return commentRepository
      .findAllByPostId(postId)
      .stream()
      .map(this::mapCommentToResponse)
      .collect(java.util.stream.Collectors.toList());
  }

  private CommentResponse mapCommentToResponse(Comment comment) {
    CommentResponse response = commentMapper.toDto(comment);

    if (response.getAuthorUsername() == null && comment.getAuthor() != null) {
      response.setAuthorUsername(comment.getAuthor().getUsername());
    }

    return response;
  }
}
