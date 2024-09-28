package com.madeeasy.service.impl;

import com.madeeasy.dto.request.FacultyProfileRequestDTO;
import com.madeeasy.dto.response.FacultyProfileResponseDTO;
import com.madeeasy.entity.FacultyProfile;
import com.madeeasy.exception.FacultyNotFoundException;
import com.madeeasy.repository.FacultyProfileRepository;
import com.madeeasy.service.FacultyProfileService;
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
public class FacultyProfileServiceImpl implements FacultyProfileService {
    private final FacultyProfileRepository facultyProfileRepository;
    private final RestTemplate restTemplate;

    @Override
    public FacultyProfileResponseDTO createFacultyProfile(MultipartFile file,
                                                          FacultyProfileRequestDTO facultyProfileRequestDTO) throws IOException {

        String departmentUrl = "http://department-service/api/department/get-department-by-id/" + facultyProfileRequestDTO.getDepartmentId();

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + authToken);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);

        DepartmentResponseDTO response =
                restTemplate.exchange(departmentUrl, HttpMethod.GET, null, DepartmentResponseDTO.class)
                        .getBody();


        String userUrl = "http://user-service/api/user/get-by-id/" + facultyProfileRequestDTO.getId();


        UserResponseDTO userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, null, UserResponseDTO.class).getBody();


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

    @Override
    public List<FacultyProfileResponseDTO> getCoursesByFacultyId(Long id) {
        //  rest-call to the course service to get the list of courses taught by the faculty
        return null;
    }

    @Override
    public FacultyProfileResponseDTO getFacultyById(Long id) {

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
