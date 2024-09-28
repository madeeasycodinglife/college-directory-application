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
public class StudentProfileRequestDTO {

    @NotNull(message = "id cannot be null")
    @Min(value = 1, message = "id must be greater than 0")
    private Long id;

    @NotNull(message = "startYear cannot be null")
    private Integer startYear;

    @NotNull(message = "endYear cannot be null")
    private Integer endYear;

    @NotNull(message = "departmentId cannot be null")
    @Min(value = 1, message = "departmentId must be greater than 0")
    private Long departmentId;
}
