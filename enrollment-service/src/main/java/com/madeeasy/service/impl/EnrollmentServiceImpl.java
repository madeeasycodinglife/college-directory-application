package com.madeeasy.service.impl;

import com.madeeasy.dto.request.EnrollmentRequestDTO;
import com.madeeasy.dto.response.EnrollmentResponseDTO;
import com.madeeasy.entity.Enrollment;
import com.madeeasy.repository.EnrollmentRepository;
import com.madeeasy.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO) {
        Enrollment enrollment = Enrollment.builder()
                .studentId(enrollmentRequestDTO.getStudentId())
                .courseId(enrollmentRequestDTO.getCourseId())
                .build();
        Enrollment savedEnrollment = this.enrollmentRepository.save(enrollment);
        return EnrollmentResponseDTO.builder()
                .id(savedEnrollment.getId())
                .studentId(savedEnrollment.getStudentId())
                .courseId(savedEnrollment.getCourseId())
                .build();
    }

    @Override
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        List<Enrollment> enrollmentList = this.enrollmentRepository.findAll();
        return enrollmentList.stream()
                .map(enrollment -> EnrollmentResponseDTO.builder()
                        .id(enrollment.getId())
                        .studentId(enrollment.getStudentId())
                        .courseId(enrollment.getCourseId())
                        .build())
                .collect(Collectors.toList());
    }
}
