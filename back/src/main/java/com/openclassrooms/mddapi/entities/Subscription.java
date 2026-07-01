package com.openclassrooms.mddapi.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "subscriptions")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString(exclude = {"user", "topic"})
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "topic_id", nullable = false)
  private Topic topic;

  @CreationTimestamp
  @Column(updatable = false, name = "created_at")
  private LocalDateTime createdAt;
}
