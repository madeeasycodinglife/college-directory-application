package com.madeeasy.service;

import com.madeeasy.dto.request.EnrollmentPartialRequestDTO;
import com.madeeasy.dto.request.EnrollmentRequestDTO;
import com.madeeasy.dto.response.EnrollmentResponseDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO);

    List<EnrollmentResponseDTO> getAllEnrollments();

    List<EnrollmentResponseDTO> getEnrollmentByCourseId(Long courseId);

    List<EnrollmentResponseDTO> getEnrollmentsByStudentId(Long studentId);

    EnrollmentResponseDTO partialUpdateEnrollment(Long enrollmentId, EnrollmentPartialRequestDTO enrollmentPartialRequestDTO);
}
