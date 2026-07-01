package com.openclassrooms.mddapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entities.Subscription;
import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
  boolean existsByUserAndTopic(User user, Topic topic);
  Optional<Subscription> findByUserAndTopic(User user, Topic topic);
}
