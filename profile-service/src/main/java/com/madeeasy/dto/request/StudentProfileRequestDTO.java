package com.madeeasy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileRequestDTO {

    private Long id;
    private int startYear;
    private int endYear;
    private Long departmentId;
}
