package com.openclassrooms.mddapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entities.Subscription;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
}
