package com.madeeasy.service.impl;

import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import com.madeeasy.entity.StudentProfile;
import com.madeeasy.exception.StudentNotFoundException;
import com.madeeasy.repository.StudentProfileRepository;
import com.madeeasy.service.StudentProfileService;
import com.madeeasy.vo.DepartmentResponseDTO;
import com.madeeasy.vo.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final RestTemplate restTemplate;

    @Override
    public StudentProfileResponseDTO getStudentProfile(Long id) {
        return null;
    }

    @Override
    public StudentProfileResponseDTO createStudentProfile(MultipartFile file,
                                                          StudentProfileRequestDTO studentProfileRequestDTO) throws IOException {

        // rest-call to know if the department exists
        // http://localhost:8083/api/department/get-department-by-id/1

        String departmentUrl = "http://department-service/api/department/get-department-by-id/" + studentProfileRequestDTO.getDepartmentId();

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + authToken);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);

        DepartmentResponseDTO departmentResponse =
                restTemplate.exchange(departmentUrl, HttpMethod.GET, null, DepartmentResponseDTO.class)
                        .getBody();


        // rest-call to user-service if the user exists
        // http://localhost:8081/api/user/get-by-id/79b3c798-1a52-4d9b-a498-df58c1703e53

        String userUrl = "http://user-service/api/user/get-by-id/" + studentProfileRequestDTO.getId();


        UserResponseDTO userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, null, UserResponseDTO.class).getBody();


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

    @Override
    public StudentProfileResponseDTO getStudentById(Long id) {
        StudentProfile studentProfile = this.studentProfileRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found with id : " + id));

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
