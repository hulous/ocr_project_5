package com.openclassrooms.mddapi.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
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
@Schema(description = "Payload used to authenticate a user by email or username")
@Accessors(chain = true)
@ToString(exclude = "password")
public class LoginUserDto {
  @Schema(description = "User email address or username", example = "john@example.com")
  @JsonAlias("login")
  @NotBlank(message = "Email or username is required")
  private String email;

  @Schema(description = "User password", example = "Str0ngP@ssword")
  @NotBlank(message = "Password is required")
  private String password;
}
