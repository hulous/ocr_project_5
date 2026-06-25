package com.openclassrooms.mddapi.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
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
  private String email;
  private String password;
}
