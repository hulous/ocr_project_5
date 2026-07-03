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
@Schema(description = "Payload used to create a new post")
@Accessors(chain = true)
@ToString
public class CreatePostDto {

  @Schema(description = "Post title", example = "How to use the platform")
  @NotBlank(message = "Title is required")
  private String title;

  @Schema(description = "Post content", example = "This post explains how to create a topic post.")
  @NotBlank(message = "Content is required")
  private String content;
}
