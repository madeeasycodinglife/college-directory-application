package com.madeeasy.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeeasy.dto.request.CourseAssignmentRequestDTO;
import com.madeeasy.dto.request.CourseRequestDTO;
import com.madeeasy.dto.response.CourseResponseDTO;
import com.madeeasy.dto.response.ResponseDTO;
import com.madeeasy.entity.Course;
import com.madeeasy.exception.CourseNotFoundException;
import com.madeeasy.repository.CourseRepository;
import com.madeeasy.service.CourseService;
import com.madeeasy.vo.DepartmentResponseDTO;
import com.madeeasy.vo.FacultyProfileResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;
    private final HttpServletRequest httpServletRequest;
    private final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final static String COURSE = "course";

    @Override
    @CacheEvict(value = COURSE, key = "'getAllCourses'")
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO) {

        logger.info("Creating course: {}", courseRequestDTO);

        Course course = Course.builder()
                .title(courseRequestDTO.getTitle())
                .courseCode(courseRequestDTO.getCourseCode())
                .description(courseRequestDTO.getDescription())
                .build();

        courseRepository.save(course);

        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(courseRequestDTO.getTitle())
                .courseCode(courseRequestDTO.getCourseCode())
                .description(courseRequestDTO.getDescription())
                .build();

    }

    @Override
    @Cacheable(value = COURSE, key = "#root.methodName", unless = "#result == null")
    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = this.courseRepository.findAll();

        if (courses.isEmpty()) {
            // Handle the case where there are no courses
            // You can return an empty list or handle it differently
            return List.of(); // or return a custom response indicating no courses found
        }

        // Map the courses to CourseResponseDTO if the list is not empty
        return courses.stream()
                .map(course -> CourseResponseDTO.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .courseCode(course.getCourseCode())
                        .description(course.getDescription())
                        .facultyId(course.getFacultyId())
                        .departmentId(course.getDepartmentId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = COURSE, key = "#id", unless = "#result == null")
    public CourseResponseDTO getCourseById(Long id) {
        logger.info("Fetching course with ID: {}", id);
        Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id : " + id));

        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .courseCode(course.getCourseCode())
                .description(course.getDescription())
                .facultyId(course.getFacultyId())
                .departmentId(course.getDepartmentId())
                .build();
    }

    @Override
    @Cacheable(value = "courseByCode", key = "#courseCode", unless = "#result == null")
    public CourseResponseDTO getCourseByCourseCode(String courseCode) {
        logger.info("Fetching course with courseCode : {}", courseCode);
        Course course = this.courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with courseCode : " + courseCode));

        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .courseCode(course.getCourseCode())
                .description(course.getDescription())
                .facultyId(course.getFacultyId())
                .departmentId(course.getDepartmentId())
                .build();
    }


    /**
     * Delete Course by ID :-
     * -----------------------
     * When you delete a course by its ID:
     * <p>
     * Should you delete its instances?
     * Yes, if you delete a course, you should delete all instances associated with that course.
     * This is because the course instances are dependent on the existence of the course.
     * If the course no longer exists, its instances are meaningless since they reference a course that is no longer available.
     * <p>
     * Logic:
     * <p>
     * -> First, retrieve the course by its ID.
     * -> If the course exists, find all instances associated with this course.
     * -> Delete the instances.
     * -> Finally, delete the course.
     */


    @Override
    @Caching(evict = {
            @CacheEvict(value = COURSE, key = "#id"),
            @CacheEvict(value = COURSE, key = "'getAllCourses'")
    })
    @Retry(name = "myRetry", fallbackMethod = "fallbackDeleteCourse")
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallbackDeleteCourse")
    public ResponseDTO deleteCourse(Long id) {
        // Check if the course exists before deleting
        if (!courseRepository.existsById(id)) {
            return new ResponseDTO("Course with ID " + id + " does not exist.", NOT_FOUND);
        }

        // Create the URL for deleting the related course instances
        String courseServiceUrl = "http://course-instance-service/api/instances/courseId/" + id;

        // Get the authorization header from the request
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        // Create HttpEntity with the token
        HttpEntity<String> requestEntity = createHttpEntityWithToken(authorizationHeader);

        logger.info("Calling course instance service to delete course instance with ID: {}", id);

        // Make the DELETE request to the course instance service
        ResponseEntity<String> response = restTemplate.exchange(courseServiceUrl, HttpMethod.DELETE, requestEntity, String.class);

        // Proceed with the deletion of the course from the primary database
        courseRepository.deleteById(id);

        logger.info("Course instance deleted successfully for course ID: {}", id);

        return new ResponseDTO("Course with ID " + id + " has been successfully deleted.", HttpStatus.OK);
    }

    @Override
    public List<CourseResponseDTO> getCoursesByFacultyId(Long id) {

        List<Course> coursesByFacultyId = this.courseRepository.findByFacultyId(id);
        log.info("{}", coursesByFacultyId);
        return coursesByFacultyId.stream()
                .map(course -> CourseResponseDTO.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .courseCode(course.getCourseCode())
                        .description(course.getDescription())
                        .facultyId(course.getFacultyId())
                        .departmentId(course.getDepartmentId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDTO> getCoursesByDepartmentId(Long id) {

        List<Course> coursesByDepartmentId = this.courseRepository.findByDepartmentId(id);
        log.info("{}", coursesByDepartmentId);
        return coursesByDepartmentId.stream()
                .map(course -> CourseResponseDTO.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .courseCode(course.getCourseCode())
                        .description(course.getDescription())
                        .facultyId(course.getFacultyId())
                        .departmentId(course.getDepartmentId())
                        .build())
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "courseAssignmentFallbackMethod")
    @Override
    public CourseResponseDTO courseAssignment(CourseAssignmentRequestDTO course) {

        // rest-call to depart-service to know if department exists
        // http://localhost:8083/api/department/get-department-by-id/1

        // rest-call to faculty-service to know if faculty exists
        // http://localhost:8082/api/faculty-profile/get-by-id/1


        String departmentUrl = "http://department-service/api/department/get-department-by-id/" + course.getDepartmentId();

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + authToken);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);

        DepartmentResponseDTO departmentResponse =
                restTemplate.exchange(departmentUrl, HttpMethod.GET, null, DepartmentResponseDTO.class)
                        .getBody();


        String facultyUrl = "http://profile-service/api/faculty-profile/get-by-id/" + course.getFacultyId();

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + authToken);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);

        FacultyProfileResponseDTO facultyResponse = restTemplate.exchange(facultyUrl, HttpMethod.GET, null, FacultyProfileResponseDTO.class)
                .getBody();


        Course foundCourse = this.courseRepository.findById(course.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id : " + course.getCourseId()));

        foundCourse.setDepartmentId(course.getDepartmentId());

        foundCourse.setFacultyId(course.getFacultyId());

        this.courseRepository.save(foundCourse);

        return CourseResponseDTO.builder()
                .id(foundCourse.getId())
                .title(foundCourse.getTitle())
                .courseCode(foundCourse.getCourseCode())
                .description(foundCourse.getDescription())
                .facultyId(foundCourse.getFacultyId())
                .departmentId(foundCourse.getDepartmentId())
                .build();
    }


    public CourseResponseDTO courseAssignmentFallbackMethod(
            CourseAssignmentRequestDTO course,
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

                    return CourseResponseDTO.builder()
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

                    return CourseResponseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMessage)
                            .build();
                } catch (Exception e) {
                    log.error("Failed to parse the error response", e);
                }
            }
        }

        // Fallback response if the exception is not HttpClientErrorException or any other case
        return CourseResponseDTO.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("Sorry !! Service is unavailable. Please try again later.")
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

    public ResponseDTO fallbackDeleteCourse(Long id, Throwable t) {
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

                    return ResponseDTO.builder()
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
                    if (((HttpClientErrorException) t).getStatusCode() == HttpStatus.NOT_FOUND) {
                        // Handle the case where the course instance was not found
                        courseRepository.deleteById(id);
                        return new ResponseDTO("Course with ID " + id + " has been successfully deleted.", HttpStatus.OK);
                    }
                    return ResponseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMessage)
                            .build();
                } catch (Exception e) {
                    log.error("Failed to parse the error response", e);
                }
            }
        }

        // Fallback response if the exception is not HttpClientErrorException or any other case
        return ResponseDTO.builder()
                .message("Sorry !! Course Deletion failed as Course Instance Service is unavailable. Please try again later.")
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }


}


