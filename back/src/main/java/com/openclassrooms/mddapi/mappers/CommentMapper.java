package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.CommentDto;
import com.openclassrooms.mddapi.entities.Comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(target = "authorId", source = "author.id")
  @Mapping(target = "postId", source = "post.id")
  CommentDto toDto(Comment comment);
}
