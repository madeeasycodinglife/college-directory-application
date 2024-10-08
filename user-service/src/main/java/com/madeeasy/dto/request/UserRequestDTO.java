package com.madeeasy.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "id cannot be blank")
    private Long id;

    @NotBlank(message = "fullName cannot be blank")
    private String fullName;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long, including at least one digit, one lowercase letter, one uppercase letter, and one special character. No spaces are allowed."
    )
    private String password;

    @NotBlank(message = "phone cannot be blank")
    @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "phone must be a valid phone number with 10 to 13 digits")
    private String phone;

    @NotEmpty(message = "roles cannot be empty")
    private String role;
}
