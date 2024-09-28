package com.madeeasy.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDTO {

    @NotBlank(message = "Department name cannot be blank")
    private String name;

    @NotBlank(message = "Department description cannot be blank")
    private String description;
}
