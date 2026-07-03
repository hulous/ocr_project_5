package com.openclassrooms.mddapi.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Detailed response payload for a post, including related comments")
@Accessors(chain = true)
@ToString(callSuper = true)
public class PostDetailResponse extends PostResponse {

  @Schema(description = "Comments attached to the post")
  private List<CommentResponse> comments;
}
