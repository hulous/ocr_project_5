package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.responses.SubscriptionResponse;
import com.openclassrooms.mddapi.entities.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
  @Mapping(target = "topicId", source = "topic.id")
  @Mapping(target = "topicTitle", source = "topic.title")
  SubscriptionResponse toDto(Subscription subscription);
}
