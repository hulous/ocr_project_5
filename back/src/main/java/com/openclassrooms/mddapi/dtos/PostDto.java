package com.openclassrooms.mddapi.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data transfer object for a post")
@Accessors(chain = true)
@ToString
public class PostDto {

  private Integer id;

  @Schema(description = "Author user id", example = "1")
  private Integer authorId;

  @Schema(description = "Topic id", example = "2")
  private Integer topicId;

  @Schema(description = "Post title", example = "How to use the platform")
  private String title;

  @Schema(description = "Post content")
  private String content;

  @Schema(description = "Creation timestamp")
  private Date createdAt;

  @Schema(description = "Last update timestamp")
  private Date updatedAt;
}
