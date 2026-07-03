package com.openclassrooms.mddapi.responses;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class UserResponse {
  private Integer id;
  private String username;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<SubscriptionResponse> subscriptions;
}
