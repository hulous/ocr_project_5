package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;
import com.openclassrooms.mddapi.responses.PostResponse;
import com.openclassrooms.mddapi.responses.TopicResponse;
import com.openclassrooms.mddapi.services.PostService;
import com.openclassrooms.mddapi.services.SubscriptionService;
import com.openclassrooms.mddapi.services.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/topics")
@RestController
@Tag(name = "Topics", description = "Topic resource endpoints")
public class TopicsController {
  private final TopicService topicService;
  private final PostService postService;
  private final SubscriptionService subscriptionService;

  public TopicsController(TopicService topicService, PostService postService, SubscriptionService subscriptionService) {
    this.topicService = topicService;
    this.postService = postService;
    this.subscriptionService = subscriptionService;
  }

  @GetMapping
  @Operation(summary = "List all topics")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of topics", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopicResponse.class)))),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<List<TopicResponse>> list() {
    return ResponseEntity.ok(topicService.listTopics());
  }

  @PostMapping("/{topicId}/subscription")
  @Operation(
    summary = "Subscribe the current authenticated user to a topic",
    security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Subscription created successfully", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class))),
    @ApiResponse(responseCode = "400", description = "Already subscribed or invalid request", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "AlreadySubscribed", value = "{\"message\": \"Already subscribed to this topic\"}")
    })),
    @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "UnauthorizedRequest", value = "{\"message\": \"Unauthorized request\"}")
    })),
    @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "TopicNotFound", value = "{\"message\": \"Topic not found\"}")
    })),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<ApiMessageResponse> subscribe(@PathVariable Integer topicId) {
    return ResponseEntity.ok(subscriptionService.subscribeCurrentUser(topicId));
  }

  @DeleteMapping("/{topicId}/subscription")
  @Operation(
    summary = "Unsubscribe the current authenticated user from a topic",
    security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Subscription removed successfully", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class))),
    @ApiResponse(responseCode = "400", description = "No existing subscription or invalid request", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "NoSubscriptionFound", value = "{\"message\": \"No subscription found for this topic\"}")
    })),
    @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "UnauthorizedRequest", value = "{\"message\": \"Unauthorized request\"}")
    })),
    @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "TopicNotFound", value = "{\"message\": \"Topic not found\"}")
    })),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<ApiMessageResponse> unsubscribe(@PathVariable Integer topicId) {
    return ResponseEntity.ok(subscriptionService.unsubscribeCurrentUser(topicId));
  }

  @GetMapping("/{topicId}/posts")
  @Operation(summary = "List all posts for a topic")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of posts", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))),
    @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class))),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<List<PostResponse>> listPosts(@PathVariable Integer topicId) {
    return ResponseEntity.ok(postService.listPostsForTopic(topicId));
  }

  @PostMapping("/{topicId}/posts")
  @Operation(
    summary = "Create a new post for a topic",
    security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Post created successfully", content = @Content(schema = @Schema(implementation = PostResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "InvalidPostPayload", value = "{\"message\": \"Title and content must be provided\"}")
    })),
    @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "UnauthorizedRequest", value = "{\"message\": \"Unauthorized request\"}")
    })),
    @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class), examples = {
      @ExampleObject(name = "TopicNotFound", value = "{\"message\": \"Topic not found\"}")
    })),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<PostResponse> createPost(
    @PathVariable Integer topicId,
    @Valid @RequestBody CreatePostDto input
  ) {
    return ResponseEntity.ok(postService.createPostForTopic(topicId, input));
  }
}
