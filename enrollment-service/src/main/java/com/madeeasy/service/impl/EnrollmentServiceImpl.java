package com.madeeasy.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeeasy.dto.request.EnrollmentRequestDTO;
import com.madeeasy.dto.response.EnrollmentResponseDTO;
import com.madeeasy.entity.Enrollment;
import com.madeeasy.repository.EnrollmentRepository;
import com.madeeasy.service.EnrollmentService;
import com.madeeasy.vo.CourseResponseDTO;
import com.madeeasy.vo.StudentProfileResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;
    private final HttpServletRequest httpServletRequest;

    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "createEnrollmentFallbackMethod")
    @Override
    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO) {


        String courseServiceUrl = "http://course-service/api/courses/";
        String studentServiceUrl = "http://student-service/api/student-profile/get-by-id/";


        // rest-call to course-service to know if course exists
        // http://localhost:8084/api/courses/1

        // Get the authorization header from the request
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        // Create HttpEntity with the token
        HttpEntity<String> requestEntity = createHttpEntityWithToken(authorizationHeader);

        // Call course service
        CourseResponseDTO courseResponse = restTemplate.exchange(
                courseServiceUrl + enrollmentRequestDTO.getCourseId(),
                HttpMethod.GET,
                requestEntity,
                CourseResponseDTO.class
        ).getBody();

        log.info("courseResponse: {}", courseResponse);

        // rest-call to student-service to know if student exists
        // http://localhost:8082/api/student-profile/get-by-id/1
        // Call student service
        StudentProfileResponseDTO studentResponse = restTemplate.exchange(
                studentServiceUrl + enrollmentRequestDTO.getStudentId(),
                HttpMethod.GET,
                requestEntity,
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

    private HttpEntity<String> createHttpEntityWithToken(String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // Strip "Bearer " prefix
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return new HttpEntity<>(headers);
    }


    public EnrollmentResponseDTO createEnrollmentFallbackMethod(
            EnrollmentRequestDTO enrollmentRequestDTO,
            Throwable t) {


        log.error("message : {}", t.getMessage());

        // Check if the throwable is an instance of HttpClientErrorException
        if (t instanceof HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                try {
                    // Parse the response body as JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(exception.getResponseBodyAsString());

                    // Extract specific fields from the JSON, such as 'message' and 'status'
                    String errorMessage = jsonNode.path("message").asText();
                    String errorStatus = jsonNode.path("status").asText();

                    // Log the extracted information
                    log.error("message : {} , status : {}", errorMessage, errorStatus);

                    return EnrollmentResponseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMessage)
                            .build();
                } catch (Exception e) {
                    log.error("Failed to parse the error response", e);
                }
            } else {
                try {
                    // Parse the response body as JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(exception.getResponseBodyAsString());

                    // Extract specific fields from the JSON, such as 'message' and 'status'
                    String errorMessage = jsonNode.path("message").asText();
                    String errorStatus = jsonNode.path("status").asText();

                    // Log the extracted information
                    log.error("message : {} , status : {}", errorMessage, errorStatus);

                    return EnrollmentResponseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMessage)
                            .build();
                } catch (Exception e) {
                    log.error("Failed to parse the error response", e);
                }
            }
        }

        // Fallback response if the exception is not HttpClientErrorException or any other case
        return EnrollmentResponseDTO.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("Sorry !! Service is unavailable. Please try again later.")
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
