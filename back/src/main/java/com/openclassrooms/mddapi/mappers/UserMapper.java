package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.responses.UserResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { SubscriptionMapper.class })
public interface UserMapper {
  UserResponse toResponse(User user);
}
