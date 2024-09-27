package com.madeeasy.service;

import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentProfileService {

    StudentProfileResponseDTO getStudentProfile(Long id);

    StudentProfileResponseDTO createStudentProfile(MultipartFile file, StudentProfileRequestDTO studentProfileRequestDTO) throws IOException;

    StudentProfileResponseDTO getPhotoById(Long id);
}
