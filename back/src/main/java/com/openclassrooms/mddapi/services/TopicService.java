package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.responses.TopicResponse;
import com.openclassrooms.mddapi.mappers.TopicMapper;
import com.openclassrooms.mddapi.repositories.TopicRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;

  public TopicService(
    TopicRepository topicRepository,
    TopicMapper topicMapper
  ) {
    this.topicRepository = topicRepository;
    this.topicMapper = topicMapper;
  }

  public List<TopicResponse> listTopics() {
    return topicRepository
      .findAll()
      .stream()
      .map(topicMapper::toDto)
      .collect(Collectors.toList());
  }

}
