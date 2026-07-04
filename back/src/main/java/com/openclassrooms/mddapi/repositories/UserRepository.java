package com.openclassrooms.mddapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.mddapi.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findByEmail(String email);
  Optional<User> findFirstByEmailOrUsername(String email, String username);
  boolean existsByEmail(String email);
}
