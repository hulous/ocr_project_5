package com.openclassrooms.mddapi.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payload used to register a new user")
@Accessors(chain = true)
@ToString(exclude = "password")
public class RegisterUserDto {
  @Schema(
    description = "User email address. Must be a valid email and include a top-level domain.",
    example = "alice@example.com",
    pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
  )
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @Schema(description = "User password. Minimum 8 characters, including one uppercase, one lowercase, one digit, and one special character.", example = "Str0ngP@ssword")
  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters")
  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,40}$",
    message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
  )
  private String password;

  @Schema(description = "Displayed username", example = "Alice Martin")
  private String username;
}
