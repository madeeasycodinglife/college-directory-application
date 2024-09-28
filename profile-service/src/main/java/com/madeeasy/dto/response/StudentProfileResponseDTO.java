package com.madeeasy.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentProfileResponseDTO {

    private Long id;
    @JsonIgnore
    private byte[] photo;
    @JsonIgnore
    private String type;
    private Long departmentId;
    private Integer startYear;
    private Integer endYear;
    private HttpStatus status;
    private String message;
}
