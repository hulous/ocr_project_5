package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.CreateCommentDto;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;
import com.openclassrooms.mddapi.responses.CommentResponse;
import com.openclassrooms.mddapi.responses.PostDetailResponse;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/posts")
@RestController
@Tag(name = "Posts", description = "Post resource endpoints")
public class PostsController {

  private final PostService postService;
  private final CommentService commentService;

  public PostsController(PostService postService, CommentService commentService) {
    this.postService = postService;
    this.commentService = commentService;
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Get one post by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Post found", content = @Content(schema = @Schema(implementation = PostDetailResponse.class))),
    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class))),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<PostDetailResponse> show(@PathVariable Integer postId) {
    return ResponseEntity.ok(postService.show(postId));
  }

  @PostMapping("/{postId}/comments")
  @Operation(
    summary = "Create a comment on a post",
    security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Comment created successfully", content = @Content(schema = @Schema(implementation = CommentResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "InvalidCommentPayload", value = "{\"message\": \"Comment text must not be empty\"}")
    })),
    @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "UnauthorizedRequest", value = "{\"message\": \"Unauthorized request\"}")
    })),
    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "PostNotFound", value = "{\"message\": \"Post not found\"}")
    })),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<CommentResponse> createComment(
    @PathVariable Integer postId,
    @Valid @RequestBody CreateCommentDto input
  ) {
    return ResponseEntity.ok(commentService.createCommentForPost(postId, input));
  }
}
