package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.TopicDto;
import com.openclassrooms.mddapi.responses.ApiMessageResponse;
import com.openclassrooms.mddapi.services.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/topics")
@RestController
@Tag(name = "Topics", description = "Topic resource endpoints")
public class TopicsController {
  private final TopicService topicService;

  public TopicsController(TopicService topicService) {
    this.topicService = topicService;
  }

  @GetMapping
  @Operation(summary = "List all topics")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of topics", content = @Content(schema = @Schema(implementation = TopicDto.class))),
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ApiMessageResponse.class)))
  })
  public ResponseEntity<List<TopicDto>> list() {
    return ResponseEntity.ok(topicService.listTopics());
  }
}
