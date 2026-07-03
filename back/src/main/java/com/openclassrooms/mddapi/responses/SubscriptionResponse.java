package com.openclassrooms.mddapi.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Response payload for a subscription")
@Accessors(chain = true)
@ToString
public class SubscriptionResponse {

  private Integer id;

  @Schema(description = "User id", example = "1")
  private Integer userId;

  @Schema(description = "Topic id", example = "2")
  private Integer topicId;

  @Schema(description = "Topic title", example = "Development")
  private String topicTitle;

  @Schema(description = "Creation timestamp")
  private LocalDateTime createdAt;
}
