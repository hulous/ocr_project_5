package com.openclassrooms.mddapi.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Response payload for a comment")
@Accessors(chain = true)
@ToString
public class CommentResponse {

  private Integer id;

  @Schema(description = "Author user id", example = "1")
  private Integer authorId;

  @Schema(description = "Author username", example = "alice")
  private String authorUsername;

  @Schema(description = "Post id", example = "3")
  private Integer postId;

  @Schema(description = "Comment content")
  private String content;

  @Schema(description = "Creation timestamp")
  private LocalDateTime createdAt;

  @Schema(description = "Last update timestamp")
  private LocalDateTime updatedAt;
}
