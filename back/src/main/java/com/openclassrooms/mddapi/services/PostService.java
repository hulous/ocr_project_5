package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.exceptions.ApiException;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.repositories.PostRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;

  public PostService(PostRepository postRepository, PostMapper postMapper) {
    this.postRepository = postRepository;
    this.postMapper = postMapper;
  }

  public PostDto show(Integer postId) {
    Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

    return postMapper.toDto(post);
  }
}
