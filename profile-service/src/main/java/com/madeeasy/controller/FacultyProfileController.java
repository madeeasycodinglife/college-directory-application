package com.madeeasy.controller;


import com.madeeasy.dto.request.FacultyProfileRequestDTO;
import com.madeeasy.dto.response.FacultyProfileResponseDTO;
import com.madeeasy.service.FacultyProfileService;
import com.madeeasy.util.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
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
