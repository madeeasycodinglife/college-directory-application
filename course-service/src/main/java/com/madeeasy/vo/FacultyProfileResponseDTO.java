package com.madeeasy.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacultyProfileResponseDTO {

    private Long id;
    @JsonIgnore
    private byte[] photo;
    @JsonIgnore
    private String type;
    private String officeHours;
    private Long departmentId;
}
