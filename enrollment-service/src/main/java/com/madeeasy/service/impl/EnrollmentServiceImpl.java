package com.madeeasy.service.impl;

import com.madeeasy.dto.request.EnrollmentRequestDTO;
import com.madeeasy.dto.response.EnrollmentResponseDTO;
import com.madeeasy.entity.Enrollment;
import com.madeeasy.repository.EnrollmentRepository;
import com.madeeasy.service.EnrollmentService;
import com.madeeasy.vo.CourseResponseDTO;
import com.madeeasy.vo.StudentProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;

    @Override
    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO) {


        String courseServiceUrl = "http://course-service/api/courses/";
        String studentServiceUrl = "http://student-service/api/student-profile/get-by-id/";


        // rest-call to course-service to know if course exists
        // http://localhost:8084/api/courses/1

        // Call course service
        CourseResponseDTO courseResponse = restTemplate.exchange(
                courseServiceUrl + enrollmentRequestDTO.getCourseId(),
                HttpMethod.GET,
                null,
                CourseResponseDTO.class
        ).getBody();

        log.info("courseResponse: {}", courseResponse);

        // rest-call to student-service to know if student exists
        // http://localhost:8082/api/student-profile/get-by-id/1
        // Call student service
        StudentProfileResponseDTO studentResponse = restTemplate.exchange(
                studentServiceUrl + enrollmentRequestDTO.getStudentId(),
                HttpMethod.GET,
                null,
                StudentProfileResponseDTO.class
        ).getBody();

        log.info("studentResponse: {}", studentResponse);


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

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentByCourseId(Long courseId) {

        List<Enrollment> enrollments = this.enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
                .map(enrollment -> EnrollmentResponseDTO.builder()
                        .id(enrollment.getId())
                        .studentId(enrollment.getStudentId())
                        .courseId(enrollment.getCourseId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByStudentId(Long studentId) {

        List<Enrollment> enrollments = this.enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(enrollment -> EnrollmentResponseDTO.builder()
                        .id(enrollment.getId())
                        .studentId(enrollment.getStudentId())
                        .courseId(enrollment.getCourseId())
                        .build())
                .collect(Collectors.toList());
    }
}
