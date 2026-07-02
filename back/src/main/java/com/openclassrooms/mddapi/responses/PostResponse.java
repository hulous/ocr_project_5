package com.openclassrooms.mddapi.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Response payload for a post")
@Accessors(chain = true)
@ToString
public class PostResponse {

  private Integer id;

  @Schema(description = "Author user id", example = "1")
  private Integer authorId;

  @Schema(description = "Author username", example = "alice")
  private String authorUsername;

  @Schema(description = "Topic id", example = "2")
  private Integer topicId;

  @Schema(description = "Topic title", example = "Development")
  private String topicTitle;

  @Schema(description = "Topic description", example = "Technical tutorials, tips, and code review discussions.")
  private String topicDescription;

  @Schema(description = "Post title", example = "How to use the platform")
  private String title;

  @Schema(description = "Post content")
  private String content;

  @Schema(description = "Creation timestamp")
  private Date createdAt;

  @Schema(description = "Last update timestamp")
  private Date updatedAt;
}
