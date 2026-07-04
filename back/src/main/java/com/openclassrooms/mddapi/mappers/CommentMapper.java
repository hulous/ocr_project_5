package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.responses.CommentResponse;
import com.openclassrooms.mddapi.entities.Comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(target = "authorId", source = "author.id")
  @Mapping(target = "authorUsername", source = "author.username")
  @Mapping(target = "postId", source = "post.id")
  CommentResponse toDto(Comment comment);
}
