package com.openclassrooms.mddapi.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payload used to authenticate a user by email or name")
@Accessors(chain = true)
@ToString(exclude = "password")
public class LoginUserDto {
  @Schema(description = "User email address or name", example = "john@example.com")
  private String email;
  private String password;
}
