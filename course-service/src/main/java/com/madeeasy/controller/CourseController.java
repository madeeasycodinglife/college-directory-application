package com.madeeasy.controller;

import com.madeeasy.dto.request.CourseAssignmentPartialRequestDTO;
import com.madeeasy.dto.request.CourseAssignmentRequestDTO;
import com.madeeasy.dto.request.CourseRequestDTO;
import com.madeeasy.dto.response.CourseResponseDTO;
import com.madeeasy.dto.response.ResponseDTO;
import com.madeeasy.service.CourseService;
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
@RequestMapping(path = "/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;


    @PostMapping(path = "/create")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseRequestDTO course) {
        CourseResponseDTO createdCourse = courseService.createCourse(course);
        if (createdCourse.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createdCourse);
        } else if (createdCourse.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createdCourse);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PatchMapping(path = "/course-assignment")
    public ResponseEntity<?> courseAssignment(@Valid @RequestBody CourseAssignmentRequestDTO course) {
        CourseResponseDTO updatedCourse = courseService.courseAssignment(course);

        if (updatedCourse.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updatedCourse);
        } else if (updatedCourse.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updatedCourse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
    }

    @PatchMapping(path = "/update-course-assignment/{courseId}")
    public ResponseEntity<?> partialUpdateCourseAssignment(@PathVariable Long courseId, @Valid @RequestBody CourseAssignmentPartialRequestDTO courseRequestDTO) {
        CourseResponseDTO courseResponseDTO = courseService.partialUpdateCourseAssignment
                (courseId, courseRequestDTO);
        if (courseResponseDTO.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(courseResponseDTO);
        } else if (courseResponseDTO.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(courseResponseDTO);
        } else if (courseResponseDTO.getStatus() == HttpStatus.CONFLICT) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(courseResponseDTO);
        } else if (courseResponseDTO.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(courseResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseResponseDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            // Return 204 No Content if there are no courses
            return ResponseEntity.status(HttpStatus.OK).body(List.of());
        }
        // Return 200 OK with the list of courses
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        CourseResponseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<?> getCourseByCourseCode(@PathVariable String courseCode) {
        Map<String, String> errors = ValidationUtils.validateCourseCode(courseCode);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        CourseResponseDTO course = courseService.getCourseByCourseCode(courseCode);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "/faculty/{id}")
    public ResponseEntity<?> getCoursesByFacultyId(@PathVariable Long id) {
        List<CourseResponseDTO> courses = courseService.getCoursesByFacultyId(id);
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }


    @GetMapping(path = "/department/{id}")
    public ResponseEntity<?> getCoursesByDepartmentId(@PathVariable Long id) {
        List<CourseResponseDTO> courses = courseService.getCoursesByDepartmentId(id);
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        ResponseDTO responseDTO = courseService.deleteCourse(id);

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
