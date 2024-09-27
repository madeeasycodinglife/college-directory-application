package com.madeeasy.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorProfileRequestDTO {
    private Long id;
    private Long departmentId;
}
