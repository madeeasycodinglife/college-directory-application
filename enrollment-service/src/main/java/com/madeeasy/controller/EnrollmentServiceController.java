package com.madeeasy.controller;

import com.madeeasy.dto.request.EnrollmentRequestDTO;
import com.madeeasy.dto.response.EnrollmentResponseDTO;
import com.madeeasy.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentServiceController {

    private final EnrollmentService enrollmentService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createEnrollment(@RequestBody EnrollmentRequestDTO enrollmentRequestDTO) {
        EnrollmentResponseDTO enrollmentResponseDTO = this.enrollmentService.createEnrollment(enrollmentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentResponseDTO);
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<?> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollments = this.enrollmentService.getAllEnrollments();
        return ResponseEntity.status(HttpStatus.OK).body(enrollments);
    }
}
