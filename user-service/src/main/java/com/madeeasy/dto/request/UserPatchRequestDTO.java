package com.madeeasy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchRequestDTO {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role;
}
