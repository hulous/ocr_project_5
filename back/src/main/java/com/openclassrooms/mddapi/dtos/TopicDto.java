package com.openclassrooms.mddapi.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data transfer object for a topic")
@Accessors(chain = true)
@ToString
public class TopicDto {

  private Integer id;

  @Schema(description = "Topic title", example = "Development")
  private String title;

  @Schema(description = "Topic description", example = "Technical tutorials, tips, and code review discussions.")
  private String description;
}
