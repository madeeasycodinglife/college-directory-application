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
import java.util.List;

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

    @Override
    public List<StudentProfileResponseDTO> getStudentsByDepartmentId(Long id) {

        List<StudentProfile> students = this.studentProfileRepository.findByDepartmentId(id);
        return students.stream()
                .map(student -> StudentProfileResponseDTO.builder()
                        .id(student.getId())
                        .photo(student.getPhoto())
                        .type(student.getType())
                        .departmentId(student.getDepartmentId())
                        .startYear(student.getStartYear())
                        .endYear(student.getEndYear())
                        .build())
                .toList();
    }

    @Override
    public List<StudentProfileResponseDTO> getStudentsByStartYearAndEndYear(Integer startYear, Integer endYear) {

        List<StudentProfile> students = this.studentProfileRepository.findByStartYearAndEndYear(startYear, endYear);
        return students.stream()
                .map(student -> StudentProfileResponseDTO.builder()
                        .id(student.getId())
                        .photo(student.getPhoto())
                        .type(student.getType())
                        .departmentId(student.getDepartmentId())
                        .startYear(student.getStartYear())
                        .endYear(student.getEndYear())
                        .build())
                .toList();
    }
}
