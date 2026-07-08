package com.openclassrooms.mddapi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entities.Topic;
import com.openclassrooms.mddapi.entities.User;
import com.openclassrooms.mddapi.responses.TopicResponse;

import java.util.List;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Integer> {
  List<Topic> findAll();

  // This query get topics and if current user had subscribed
  // it set a Topic.subscribed to true (for each Topic loaded).
  @Query("select new com.openclassrooms.mddapi.responses.TopicResponse(t.id, t.title, t.description, case when count(s) > 0 then true else false end) "
     + "from Topic t "
     + "left join t.subscriptions s on s.user = :user "
     + "group by t.id, t.title, t.description")
  List<TopicResponse> findAllWithSubscribedFlagByUser(@Param("user") User user);
}
