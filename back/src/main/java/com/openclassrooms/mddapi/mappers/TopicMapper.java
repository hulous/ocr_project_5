package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.responses.TopicResponse;
import com.openclassrooms.mddapi.entities.Topic;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {
  TopicResponse toDto(Topic topic);
}
