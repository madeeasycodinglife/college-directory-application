package com.madeeasy.service;

import com.madeeasy.dto.request.FacultyPartialProfileRequestDTO;
import com.madeeasy.dto.request.FacultyProfileRequestDTO;
import com.madeeasy.dto.response.FacultyProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FacultyProfileService {

    FacultyProfileResponseDTO createFacultyProfile(MultipartFile file, FacultyProfileRequestDTO facultyProfileRequestDTO) throws IOException;

    FacultyProfileResponseDTO getPhotoById(Long id);

    List<FacultyProfileResponseDTO> getCoursesByFacultyId(Long id);

    FacultyProfileResponseDTO getFacultyById(Long id);

    FacultyProfileResponseDTO partiallyUpdateUser(Long id, FacultyPartialProfileRequestDTO facultyProfileRequestDTO);

    List<FacultyProfileResponseDTO> getFacultiesByDepartmentId(Long id);
}
