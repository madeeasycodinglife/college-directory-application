package com.madeeasy.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentPartialRequestDTO {

    @Min(value = 1, message = "studentId must be greater than 0")
    private Long studentId;

    @Min(value = 1, message = "courseId must be greater than 0")
    private Long courseId;
}
