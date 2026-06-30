package com.openclassrooms.mddapi.dtos;

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
@Schema(description = "Data transfer object for a subscription")
@Accessors(chain = true)
@ToString
public class SubscriptionDto {

  private Integer id;

  @Schema(description = "User id", example = "1")
  private Integer userId;

  @Schema(description = "Topic id", example = "2")
  private Integer topicId;

  @Schema(description = "Creation timestamp")
  private LocalDateTime createdAt;
}
