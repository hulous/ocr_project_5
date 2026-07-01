package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.TopicDto;
import com.openclassrooms.mddapi.entities.Topic;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {
  TopicDto toDto(Topic topic);
}
