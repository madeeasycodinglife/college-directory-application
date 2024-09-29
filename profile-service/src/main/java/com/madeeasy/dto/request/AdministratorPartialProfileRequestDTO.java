package com.madeeasy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorPartialProfileRequestDTO {

    @Min(value = 1, message = "departmentId must be greater than 0")
    private Long departmentId;
}
