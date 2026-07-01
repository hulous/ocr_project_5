package com.openclassrooms.mddapi.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payload used to create a new comment")
@Accessors(chain = true)
@ToString
public class CreateCommentDto {

  @Schema(description = "Comment content", example = "I found this article very useful.")
  @NotBlank(message = "Content is required")
  private String content;
}
