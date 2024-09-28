package com.madeeasy.controller;

import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import com.madeeasy.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/student-profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createStudentProfile(@RequestParam("file") MultipartFile file,
                                                  StudentProfileRequestDTO studentProfileRequestDTO) throws IOException {
        StudentProfileResponseDTO studentProfile = studentProfileService.createStudentProfile(file, studentProfileRequestDTO);

        // Prepare the response with the image and additional data
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentProfile);
    }

    @GetMapping(path = "/get-photo-by-id/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        StudentProfileResponseDTO studentProfileResponseDTO = this.studentProfileService.getPhotoById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(studentProfileResponseDTO.getType()))
                .body(new ByteArrayResource(studentProfileResponseDTO.getPhoto()));
    }

    @GetMapping(path = "/get-by-department-id/{id}")
    public ResponseEntity<?> getStudentsByDepartmentId(@PathVariable Long id) {
        List<StudentProfileResponseDTO> students = this.studentProfileService.getStudentsByDepartmentId(id);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping(path = "/get-by-start-year-and-end-year/{startYear}/{endYear}")
    public ResponseEntity<?> getStudentsByStartYearAndEndYear(
            @PathVariable Integer startYear,
            @PathVariable Integer endYear
    ) {
        List<StudentProfileResponseDTO> students = this.studentProfileService.getStudentsByStartYearAndEndYear(startYear, endYear);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }
}
