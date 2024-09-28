package com.madeeasy.controller;

import com.madeeasy.dto.request.EnrollmentRequestDTO;
import com.madeeasy.dto.response.EnrollmentResponseDTO;
import com.madeeasy.service.EnrollmentService;
import com.madeeasy.util.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping(path = "/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentServiceController {

    private final EnrollmentService enrollmentService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createEnrollment(@Valid @RequestBody EnrollmentRequestDTO enrollmentRequestDTO) {
        EnrollmentResponseDTO enrollmentResponseDTO = this.enrollmentService.createEnrollment(enrollmentRequestDTO);

        if (enrollmentResponseDTO.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(enrollmentResponseDTO);
        } else if (enrollmentResponseDTO.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(enrollmentResponseDTO);
        }else if (enrollmentResponseDTO.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(enrollmentResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(enrollmentResponseDTO);
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<?> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollments = this.enrollmentService.getAllEnrollments();
        return ResponseEntity.status(HttpStatus.OK).body(enrollments);
    }

    @GetMapping(path = "/get-enrollments-by-course-id/{courseId}")
    public ResponseEntity<?> getEnrollmentByCourseId(@PathVariable Long courseId) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(courseId.intValue(), "courseId");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        List<EnrollmentResponseDTO> enrollments = this.enrollmentService.getEnrollmentByCourseId(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(enrollments);
    }

    @GetMapping(path = "/get-enrollments-by-student-id/{studentId}")
    public ResponseEntity<?> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(studentId.intValue(), "studentId");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        List<EnrollmentResponseDTO> enrollments = this.enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(enrollments);
    }
}
