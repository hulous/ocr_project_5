package com.openclassrooms.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.openclassrooms.mddapi.dtos.LoginUserDto;
import com.openclassrooms.mddapi.dtos.RegisterUserDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:env.test.properties")
@Transactional
class TopicSubscriptionIntegrationTest {

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
  void subscribeAndUnsubscribeToTopicWithAuthenticatedUser() throws Exception {
    RegisterUserDto registerRequest = new RegisterUserDto()
      .setEmail("jane@example.com")
      .setUsername("jane")
      .setPassword("pwd");

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerRequest)))
      .andExpect(status().isOk());

    LoginUserDto loginRequest = new LoginUserDto()
      .setEmail("jane@example.com")
      .setPassword("pwd");

    MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").isNotEmpty())
      .andReturn();

    String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

    int topicId = 1;

    mockMvc.perform(post("/api/topics/" + topicId + "/subscription")
        .header("Authorization", "Bearer " + token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Subscription created successfully"));

    mockMvc.perform(delete("/api/topics/" + topicId + "/subscription")
        .header("Authorization", "Bearer " + token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Subscription removed successfully"));
  }

  @Test
  void subscribeWithInvalidTokenIsUnauthorized() throws Exception {
    mockMvc.perform(post("/api/topics/1/subscription")
        .header("Authorization", "Bearer invalid.jwt.token"))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("Unauthorized request"));
  }
}
