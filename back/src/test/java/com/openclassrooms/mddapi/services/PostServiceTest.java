package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.responses.CommentResponse;
import com.openclassrooms.mddapi.responses.PostDetailResponse;
import com.openclassrooms.mddapi.responses.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @Mock
  private TopicRepository topicRepository;

  @Mock
  private PostMapper postMapper;

  @Mock
  private CommentService commentService;

  @Mock
  private CurrentUserService currentUserService;

  @InjectMocks
  private PostService service;

  @Test
  void shouldShowPostDetailWithCommentsWhenPostExists() {
    Integer postId = 3;
    Post post = new Post().setId(postId);
    PostDetailResponse detailResponse = new PostDetailResponse();
    detailResponse.setId(postId);
    CommentResponse commentResponse = new CommentResponse().setId(5);

    given(postRepository.findById(postId)).willReturn(Optional.of(post));
    given(postMapper.toDetailResponse(post)).willReturn(detailResponse);
    given(commentService.listCommentsForPost(postId)).willReturn(List.of(commentResponse));

    PostDetailResponse result = service.show(postId);

    assertSame(detailResponse, result);
    assertEquals(1, result.getComments().size());
    assertEquals(5, result.getComments().get(0).getId());
  }

  @Test
  void shouldCreatePostForTopicWhenTopicExistsAndUserIsAuthenticated() {
    Integer topicId = 8;
    Topic topic = new Topic().setId(topicId);
    User author = new User().setId(12).setUsername("carol");
    CreatePostDto input = new CreatePostDto().setTitle("Hello World").setContent("Content here");
    Post savedPost = new Post().setId(101).setTopic(topic).setAuthor(author).setTitle("Hello World").setContent("Content here");
    PostResponse response = new PostResponse().setId(101).setTitle("Hello World");

    given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));
    given(currentUserService.getCurrentUser()).willReturn(author);
    given(postRepository.save(any(Post.class))).willReturn(savedPost);
    given(postMapper.toResponse(savedPost)).willReturn(response);

    PostResponse result = service.createPostForTopic(topicId, input);

    assertEquals(101, result.getId());
    assertEquals("Hello World", result.getTitle());
    verify(postRepository).save(argThat(post ->
      post.getTopic() == topic &&
      post.getAuthor() == author &&
      "Hello World".equals(post.getTitle()) &&
      "Content here".equals(post.getContent())
    ));
  }

  @Test
  void shouldListPostsForTopicWhenTopicExists() {
    Integer topicId = 21;
    Topic topic = new Topic().setId(topicId);
    Post post = new Post().setId(88).setTopic(topic);
    PostResponse response = new PostResponse().setId(88).setTitle("Topic Post");

    given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));
    given(postRepository.findAllByTopicId(topicId)).willReturn(List.of(post));
    given(postMapper.toResponse(post)).willReturn(response);

    List<PostResponse> results = service.listPostsForTopic(topicId);

    assertEquals(1, results.size());
    assertEquals(88, results.get(0).getId());
    assertEquals("Topic Post", results.get(0).getTitle());
  }
}
