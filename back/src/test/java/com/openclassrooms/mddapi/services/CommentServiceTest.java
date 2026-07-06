package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CreateCommentDto;
import com.openclassrooms.mddapi.entities.Comment;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.mappers.CommentMapper;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.responses.CommentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private PostRepository postRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private CommentMapper commentMapper;

  @Mock
  private CurrentUserService currentUserService;

  @InjectMocks
  private CommentService service;

  @Test
  void shouldCreateCommentForPostWhenPostExistsAndUserIsAuthenticated() {
    Integer postId = 42;
    CreateCommentDto input = new CreateCommentDto().setContent("This is great!");
    Post post = new Post().setId(postId);
    User author = new User().setId(7).setUsername("alice");
    Comment savedComment = new Comment().setId(100).setPost(post).setAuthor(author).setContent("This is great!");
    CommentResponse commentResponse = new CommentResponse()
      .setId(100)
      .setContent("This is great!")
      .setPostId(postId);

    given(postRepository.findById(postId)).willReturn(Optional.of(post));
    given(currentUserService.getCurrentUser()).willReturn(author);
    given(commentRepository.save(any(Comment.class))).willReturn(savedComment);
    given(commentMapper.toDto(savedComment)).willReturn(commentResponse);

    CommentResponse result = service.createCommentForPost(postId, input);

    assertEquals(100, result.getId());
    assertEquals("This is great!", result.getContent());
    assertEquals("alice", result.getAuthorUsername());
    verify(commentRepository).save(argThat(comment ->
      comment.getPost() == post &&
      comment.getAuthor() == author &&
      "This is great!".equals(comment.getContent())
    ));
  }

  @Test
  void shouldReturnCommentResponsesWithAuthorUsernamesForPost() {
    Integer postId = 17;
    User author = new User().setId(9).setUsername("bob");
    Comment comment = new Comment().setId(200).setAuthor(author).setContent("Nice post!");
    CommentResponse commentResponse = new CommentResponse()
      .setId(200)
      .setContent("Nice post!");

    given(commentRepository.findAllByPostId(postId)).willReturn(List.of(comment));
    given(commentMapper.toDto(comment)).willReturn(commentResponse);

    List<CommentResponse> results = service.listCommentsForPost(postId);

    assertEquals(1, results.size());
    assertEquals(200, results.get(0).getId());
    assertEquals("bob", results.get(0).getAuthorUsername());
  }
}
