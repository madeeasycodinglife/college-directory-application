package com.madeeasy.controller;


import com.madeeasy.dto.request.FacultyProfileRequestDTO;
import com.madeeasy.dto.response.FacultyProfileResponseDTO;
import com.madeeasy.service.FacultyProfileService;
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
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping(path = "/api/faculty-profile")
@RequiredArgsConstructor
public class FacultyProfileController {

    private final FacultyProfileService facultyProfileService;


    @PostMapping(path = "/create")
    public ResponseEntity<?> createFacultyProfile(@RequestParam("file") MultipartFile file,
                                                  @Valid FacultyProfileRequestDTO facultyProfileRequestDTO) throws IOException {
        FacultyProfileResponseDTO facultyProfileResponseDTO = this.facultyProfileService.createFacultyProfile(file, facultyProfileRequestDTO);

        if (facultyProfileResponseDTO.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(facultyProfileResponseDTO);
        } else if (facultyProfileResponseDTO.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(facultyProfileResponseDTO);
        } else if (facultyProfileResponseDTO.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(facultyProfileResponseDTO);
        }

        // Prepare the response with the image and additional data
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(facultyProfileResponseDTO);
    }

    @GetMapping(path = "/get-photo-by-id/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        FacultyProfileResponseDTO facultyProfileResponseDTO = this.facultyProfileService.getPhotoById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(facultyProfileResponseDTO.getType()))
                .body(new ByteArrayResource(facultyProfileResponseDTO.getPhoto()));
    }

    @GetMapping(path = "/get-by-id/{id}")
    public ResponseEntity<?> getFacultyId(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        FacultyProfileResponseDTO facultyProfileResponseDTO = this.facultyProfileService.getFacultyById(id);
        return ResponseEntity.ok()
                .body(facultyProfileResponseDTO);
    }

    // /{id}/courses: Get all courses taught by the faculty member.

    @GetMapping(path = "/{id}/courses")
    public ResponseEntity<?> getCoursesByFacultyId(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        List<FacultyProfileResponseDTO> courses = this.facultyProfileService.getCoursesByFacultyId(id);

        return ResponseEntity.ok()
                .body(courses);
    }
}
