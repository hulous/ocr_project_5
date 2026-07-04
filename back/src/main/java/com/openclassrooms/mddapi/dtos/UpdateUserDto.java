package com.openclassrooms.mddapi.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payload used to update the authenticated user profile")
@Accessors(chain = true)
@ToString(exclude = "password")
public class UpdateUserDto {

  @Schema(description = "User email address. Must be a valid email and include a top-level domain.", example = "alice@example.com", pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
  @Email(message = "Invalid email format")
  private String email;

  @Schema(description = "User password", example = "Str0ngP@ssword")
  private String password;

  @Schema(description = "Displayed username", example = "Alice Martin")
  private String username;
}
