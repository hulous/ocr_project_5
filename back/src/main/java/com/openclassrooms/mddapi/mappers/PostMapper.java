package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.entities.Post;

import org.springframework.stereotype.Component;

@Component
public class PostMapper {

  public PostDto toDto(Post post) {
    if (post == null) {
      return null;
    }

    return new PostDto()
      .setId(post.getId())
      .setAuthorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
      .setTopicId(post.getTopic() != null ? post.getTopic().getId() : null)
      .setTitle(post.getTitle())
      .setContent(post.getContent())
      .setCreatedAt(post.getCreatedAt())
      .setUpdatedAt(post.getUpdatedAt());
  }
}
