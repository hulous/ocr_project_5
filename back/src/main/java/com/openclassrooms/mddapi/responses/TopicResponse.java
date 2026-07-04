package com.openclassrooms.mddapi.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Response payload for a topic")
@Accessors(chain = true)
@ToString
public class TopicResponse {

  private Integer id;

  @Schema(description = "Topic title", example = "Development")
  private String title;

  @Schema(description = "Topic description", example = "Technical tutorials, tips, and code review discussions.")
  private String description;

  @Schema(description = "Whether the current user is subscribed to the topic", example = "true")
  private boolean subscribed;
}
