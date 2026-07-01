package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CommentDto;
import com.openclassrooms.mddapi.dtos.CreateCommentDto;
import com.openclassrooms.mddapi.entities.Comment;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.exceptions.ApiException;
import com.openclassrooms.mddapi.mappers.CommentMapper;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

  public CommentDto createCommentForPost(Integer postId, CreateCommentDto input) {
    Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

    User currentUser = currentUserService.getCurrentUser();

    Comment comment = new Comment()
      .setPost(post)
      .setAuthor(currentUser)
      .setContent(input.getContent());

    return commentMapper.toDto(commentRepository.save(comment));
  }
}
