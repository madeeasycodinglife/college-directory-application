package com.madeeasy.controller;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
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
