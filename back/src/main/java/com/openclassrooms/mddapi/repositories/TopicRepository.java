package com.openclassrooms.mddapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entities.Topic;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Integer> {
}
