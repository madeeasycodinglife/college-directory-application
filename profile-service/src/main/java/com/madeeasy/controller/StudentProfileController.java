package com.madeeasy.controller;

import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import com.madeeasy.service.StudentProfileService;
import com.madeeasy.util.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;


@Validated
@RestController
@RequestMapping(path = "/api/student-profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createStudentProfile(@RequestParam("file") MultipartFile file,
                                                  @Valid StudentProfileRequestDTO studentProfileRequestDTO) throws IOException {

        Map<String, String> errors = new HashMap<>();

        // Validate startYear
        errors.putAll(ValidationUtils.validateYear(studentProfileRequestDTO.getStartYear().toString(), "Start Year"));

        // Validate endYear
        errors.putAll(ValidationUtils.validateYear(studentProfileRequestDTO.getEndYear().toString(), "End Year"));

        // Check for validation errors
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        StudentProfileResponseDTO studentProfile = studentProfileService.createStudentProfile(file, studentProfileRequestDTO);

        if (studentProfile.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(studentProfile);
        } else if (studentProfile.getStatus() == NOT_FOUND) {
            return ResponseEntity.status(NOT_FOUND).body(studentProfile);
        } else if (studentProfile.getStatus() == SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(SERVICE_UNAVAILABLE).body(studentProfile);
        }

        // Prepare the response with the image and additional data
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentProfile);
    }

    @GetMapping(path = "/get-by-id/{id}")
    public ResponseEntity<?> getStudentId(@PathVariable Long id) {
        Map<String, String> roleValidationErrors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");
        if (!roleValidationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleValidationErrors);
        }
        StudentProfileResponseDTO studentProfileResponseDTO = this.studentProfileService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(studentProfileResponseDTO);
    }

    @GetMapping(path = "/get-photo-by-id/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        Map<String, String> roleValidationErrors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");
        if (!roleValidationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleValidationErrors);
        }
        StudentProfileResponseDTO studentProfileResponseDTO = this.studentProfileService.getPhotoById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(studentProfileResponseDTO.getType()))
                .body(new ByteArrayResource(studentProfileResponseDTO.getPhoto()));
    }

    @GetMapping(path = "/get-by-department-id/{id}")
    public ResponseEntity<?> getStudentsByDepartmentId(@PathVariable Long id) {
        Map<String, String> roleValidationErrors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");
        if (!roleValidationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleValidationErrors);
        }
        List<StudentProfileResponseDTO> students = this.studentProfileService.getStudentsByDepartmentId(id);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping(path = "/get-by-start-year-and-end-year/{startYear}/{endYear}")
    public ResponseEntity<?> getStudentsByStartYearAndEndYear(
            @PathVariable Integer startYear,
            @PathVariable Integer endYear
    ) {
        Map<String, String> errors = new HashMap<>();

        // Validate startYear
        errors.putAll(ValidationUtils.validateYear(startYear.toString(), "Start Year"));

        // Validate endYear
        errors.putAll(ValidationUtils.validateYear(endYear.toString(), "End Year"));

        // Check for validation errors
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        List<StudentProfileResponseDTO> students = this.studentProfileService.getStudentsByStartYearAndEndYear(startYear, endYear);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }
}
