package com.madeeasy.dto.response;


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
@JsonInclude(JsonInclude.Include.NON_NULL) // Include only non-null properties
public class DepartmentResponseDTO {

    private Long id;
    private String name;
    private String description;
    private HttpStatus status;
    private String message;
}
