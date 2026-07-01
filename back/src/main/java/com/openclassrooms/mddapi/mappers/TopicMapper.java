package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.TopicDto;
import com.openclassrooms.mddapi.entities.Topic;

import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

  public TopicDto toDto(Topic topic) {
    if (topic == null) {
      return null;
    }

    return new TopicDto()
      .setId(topic.getId())
      .setTitle(topic.getTitle())
      .setDescription(topic.getDescription());
  }
}
