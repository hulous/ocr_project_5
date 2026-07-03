package com.openclassrooms.mddapi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entities.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
  List<Post> findAllByTopicId(Integer topicId);
}
