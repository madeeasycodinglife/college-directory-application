package com.madeeasy.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorProfileRequestDTO {

    @NotNull(message = "id cannot be null")
    @Min(value = 1, message = "id must be greater than 0")
    private Long id;

    @NotNull(message = "departmentId cannot be null")
    @Min(value = 1, message = "departmentId must be greater than 0")
    private Long departmentId;
}
