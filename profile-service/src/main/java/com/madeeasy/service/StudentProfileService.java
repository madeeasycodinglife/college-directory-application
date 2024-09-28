package com.madeeasy.service;

import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentProfileService {

    StudentProfileResponseDTO getStudentProfile(Long id);

    StudentProfileResponseDTO createStudentProfile(MultipartFile file, StudentProfileRequestDTO studentProfileRequestDTO) throws IOException;

    StudentProfileResponseDTO getPhotoById(Long id);

    List<StudentProfileResponseDTO> getStudentsByDepartmentId(Long id);

    List<StudentProfileResponseDTO> getStudentsByStartYearAndEndYear(Integer startYear, Integer endYear);

}
