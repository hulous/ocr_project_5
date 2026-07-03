package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.responses.PostDetailResponse;
import com.openclassrooms.mddapi.responses.PostResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(target = "authorId", source = "author.id")
  @Mapping(target = "authorUsername", source = "author.username")
  @Mapping(target = "topicId", source = "topic.id")
  @Mapping(target = "topicTitle", source = "topic.title")
  @Mapping(target = "topicDescription", source = "topic.description")
  PostResponse toResponse(Post post);

  @Mapping(target = "authorId", source = "author.id")
  @Mapping(target = "authorUsername", source = "author.username")
  @Mapping(target = "topicId", source = "topic.id")
  @Mapping(target = "topicTitle", source = "topic.title")
  @Mapping(target = "topicDescription", source = "topic.description")
  PostDetailResponse toDetailResponse(Post post);
}
