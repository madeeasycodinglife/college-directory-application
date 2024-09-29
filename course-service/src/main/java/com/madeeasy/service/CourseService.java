package com.madeeasy.service;

import com.madeeasy.dto.request.CourseAssignmentPartialRequestDTO;
import com.madeeasy.dto.request.CourseAssignmentRequestDTO;
import com.madeeasy.dto.request.CourseRequestDTO;
import com.madeeasy.dto.response.CourseResponseDTO;
import com.madeeasy.dto.response.ResponseDTO;

import java.util.List;

public interface CourseService {
    CourseResponseDTO createCourse(CourseRequestDTO course);

    List<CourseResponseDTO> getAllCourses();

    CourseResponseDTO getCourseById(Long id);

    CourseResponseDTO getCourseByCourseCode(String courseCode);

    ResponseDTO deleteCourse(Long id);

    List<CourseResponseDTO> getCoursesByFacultyId(Long id);

    List<CourseResponseDTO> getCoursesByDepartmentId(Long id);

    CourseResponseDTO courseAssignment(CourseAssignmentRequestDTO course);

    CourseResponseDTO partialUpdateCourseAssignment(Long courseId, CourseAssignmentPartialRequestDTO courseRequestDTO);
}
