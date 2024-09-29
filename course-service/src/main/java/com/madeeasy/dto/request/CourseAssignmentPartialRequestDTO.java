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
public class CourseAssignmentPartialRequestDTO {

    @Min(value = 1, message = "facultyId must be greater than 0")
    private Long facultyId;

    @Min(value = 1, message = "departmentId must be greater than 0")
    private Long departmentId;
}
