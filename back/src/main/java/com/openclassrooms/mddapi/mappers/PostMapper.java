package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.entities.Post;
import com.openclassrooms.mddapi.responses.PostDetailResponse;
import com.openclassrooms.mddapi.responses.PostResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(target = "authorId", source = "author.id")
  @Mapping(target = "topicId", source = "topic.id")
  PostDto toDto(Post post);
}
