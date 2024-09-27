package com.madeeasy.service.impl;

import com.madeeasy.dto.request.FacultyProfileRequestDTO;
import com.madeeasy.dto.response.FacultyProfileResponseDTO;
import com.madeeasy.entity.FacultyProfile;
import com.madeeasy.exception.FacultyNotFoundException;
import com.madeeasy.repository.FacultyProfileRepository;
import com.madeeasy.service.FacultyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class FacultyProfileServiceImpl implements FacultyProfileService {
    private final FacultyProfileRepository facultyProfileRepository;

    @Override
    public FacultyProfileResponseDTO createFacultyProfile(MultipartFile file,
                                                          FacultyProfileRequestDTO facultyProfileRequestDTO) throws IOException {
        FacultyProfile facultyProfile = FacultyProfile.builder()
                .id(facultyProfileRequestDTO.getId())
                .photo(file.getBytes())
                .type(file.getContentType())
                .departmentId(facultyProfileRequestDTO.getDepartmentId())
                .officeHours(facultyProfileRequestDTO.getOfficeHours())
                .build();
        FacultyProfile savedFacultyProfile = this.facultyProfileRepository.save(facultyProfile);
        return FacultyProfileResponseDTO.builder()
                .id(savedFacultyProfile.getId())
                .photo(savedFacultyProfile.getPhoto())
                .type(savedFacultyProfile.getType())
                .departmentId(savedFacultyProfile.getDepartmentId())
                .officeHours(savedFacultyProfile.getOfficeHours())
                .build();
    }

    @Override
    public FacultyProfileResponseDTO getPhotoById(Long id) {
        FacultyProfile facultyProfile = this.facultyProfileRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException("Faculty not found with id : " + id));
        return FacultyProfileResponseDTO.builder()
                .id(facultyProfile.getId())
                .photo(facultyProfile.getPhoto())
                .type(facultyProfile.getType())
                .departmentId(facultyProfile.getDepartmentId())
                .officeHours(facultyProfile.getOfficeHours())
                .build();
    }
}
