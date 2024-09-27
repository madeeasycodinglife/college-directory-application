package com.madeeasy.service.impl;

import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import com.madeeasy.entity.StudentProfile;
import com.madeeasy.exception.StudentNotFoundException;
import com.madeeasy.repository.StudentProfileRepository;
import com.madeeasy.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;

    @Override
    public StudentProfileResponseDTO getStudentProfile(Long id) {
        return null;
    }

    @Override
    public StudentProfileResponseDTO createStudentProfile(MultipartFile file,
                                                          StudentProfileRequestDTO studentProfileRequestDTO) throws IOException {
        StudentProfile studentProfile = StudentProfile.builder()
                .id(studentProfileRequestDTO.getId())
                .photo(file.getBytes())
                .type(file.getContentType())
                .departmentId(studentProfileRequestDTO.getDepartmentId())
                .startYear(studentProfileRequestDTO.getStartYear())
                .endYear(studentProfileRequestDTO.getEndYear())
                .build();
        StudentProfile savedStudentProfile = this.studentProfileRepository.save(studentProfile);

        return StudentProfileResponseDTO.builder()
                .id(savedStudentProfile.getId())
                .photo(savedStudentProfile.getPhoto())
                .departmentId(savedStudentProfile.getDepartmentId())
                .startYear(savedStudentProfile.getStartYear())
                .endYear(savedStudentProfile.getEndYear())
                .build();
    }

    @Override
    public StudentProfileResponseDTO getPhotoById(Long id) {
        StudentProfile studentProfile = this.studentProfileRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id : " + id));
        return StudentProfileResponseDTO.builder()
                .id(studentProfile.getId())
                .photo(studentProfile.getPhoto())
                .type(studentProfile.getType())
                .departmentId(studentProfile.getDepartmentId())
                .startYear(studentProfile.getStartYear())
                .endYear(studentProfile.getEndYear())
                .build();
    }
}
