package com.openclassrooms.mddapi.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Response returned after successful authentication")
@Accessors(chain = true)
@ToString
public class LoginResponse {
  @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR...")
  private String token;

  @Schema(description = "Token expiration time in seconds", example = "3600")
  private long expiresIn;
}
