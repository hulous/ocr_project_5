package com.openclassrooms.mddapi.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Table(name = "topics")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString(exclude = "subscriptions")
public class Topic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String description;

  @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<Subscription> subscriptions = new ArrayList<>();
}
