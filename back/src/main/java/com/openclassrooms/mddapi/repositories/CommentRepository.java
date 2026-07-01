package com.openclassrooms.mddapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entities.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
