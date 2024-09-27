package com.madeeasy.service;

import com.madeeasy.dto.request.FacultyProfileRequestDTO;
import com.madeeasy.dto.response.FacultyProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FacultyProfileService {

    FacultyProfileResponseDTO createFacultyProfile(MultipartFile file, FacultyProfileRequestDTO facultyProfileRequestDTO) throws IOException;

    FacultyProfileResponseDTO getPhotoById(Long id);
}
