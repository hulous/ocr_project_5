package com.openclassrooms.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dtos.CreatePostDto;
import com.openclassrooms.mddapi.dtos.LoginUserDto;
import com.openclassrooms.mddapi.dtos.RegisterUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:env.test.properties")
@Transactional
class TopicPostCreationIntegrationTest {

  @Autowired
  private WebApplicationContext context;
  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
      .apply(springSecurity())
      .build();
  }

  @Test
  void createPostForTopicAsAuthenticatedUserReturnsPostDetails() throws Exception {
    RegisterUserDto registerRequest = new RegisterUserDto()
      .setEmail("postuser@example.com")
      .setUsername("postuser")
      .setPassword("pwd");

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerRequest)))
      .andExpect(status().isOk());

    LoginUserDto loginRequest = new LoginUserDto()
      .setEmail("postuser@example.com")
      .setPassword("pwd");

    MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").isNotEmpty())
      .andReturn();

    String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();
    CreatePostDto createPostRequest = new CreatePostDto()
      .setTitle("Integration test post")
      .setContent("This post was created during integration testing.");

    MvcResult postResult = mockMvc.perform(post("/api/topics/1/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(createPostRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.authorUsername").value("postuser"))
      .andExpect(jsonPath("$.topicId").value(1))
      .andExpect(jsonPath("$.title").value("Integration test post"))
      .andExpect(jsonPath("$.content").value("This post was created during integration testing."))
      .andReturn();

    Integer createdPostId = objectMapper.readTree(postResult.getResponse().getContentAsString()).get("id").asInt();

    mockMvc.perform(get("/api/topics/1/posts")
        .header("Authorization", "Bearer " + token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[?(@.id == " + createdPostId + ")].title").value("Integration test post"));
  }
}
