package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.responses.UserResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SubscriptionMapper.class })
public interface UserMapper {
  @Mapping(target = "subscriptions", ignore = true)
  UserResponse toResponse(User user);
}
