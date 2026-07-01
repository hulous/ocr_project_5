package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.PostDto;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;
import com.openclassrooms.mddapi.services.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/posts")
@RestController
@Tag(name = "Posts", description = "Post resource endpoints")
public class PostsController {

  private final PostService postService;

  public PostsController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Get one post by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Post found", content = @Content(schema = @Schema(implementation = PostDto.class))),
    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class))),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<PostDto> show(@PathVariable Integer postId) {
    return ResponseEntity.ok(postService.show(postId));
  }
}
