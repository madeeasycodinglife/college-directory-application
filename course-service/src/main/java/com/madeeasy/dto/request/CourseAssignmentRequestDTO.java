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
public class CourseAssignmentRequestDTO {

    @NotNull(message = "courseId cannot be null")
    @Min(value = 1, message = "courseId must be greater than 0")
    private Long courseId;

    @NotNull(message = "facultyId cannot be null")
    @Min(value = 1, message = "facultyId must be greater than 0")
    private Long facultyId;

    @NotNull(message = "departmentId cannot be null")
    @Min(value = 1, message = "departmentId must be greater than 0")
    private Long departmentId;
}
