package com.madeeasy.service.impl;

import com.madeeasy.dto.request.AdministratorProfileRequestDTO;
import com.madeeasy.dto.response.AdministratorProfileResponseDTO;
import com.madeeasy.entity.AdministratorProfile;
import com.madeeasy.exception.AdministratorNotFoundException;
import com.madeeasy.repository.AdministratorProfileRepository;
import com.madeeasy.service.AdministratorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdministratorProfileServiceImpl implements AdministratorProfileService {
    private final AdministratorProfileRepository administratorProfileRepository;

    @Override
    public AdministratorProfileResponseDTO createAdministratorProfile(MultipartFile file, AdministratorProfileRequestDTO administratorProfileRequestDTO) throws IOException {
        AdministratorProfile administratorProfile = AdministratorProfile.builder()
                .id(administratorProfileRequestDTO.getId())
                .photo(file.getBytes())
                .type(file.getContentType())
                .departmentId(administratorProfileRequestDTO.getDepartmentId())
                .build();

        AdministratorProfile savedAdministratorProfile = this.administratorProfileRepository.save(administratorProfile);
        return AdministratorProfileResponseDTO.builder()
                .id(savedAdministratorProfile.getId())
                .photo(savedAdministratorProfile.getPhoto())
                .type(savedAdministratorProfile.getType())
                .departmentId(savedAdministratorProfile.getDepartmentId())
                .build();
    }

    @Override
    public AdministratorProfileResponseDTO getPhotoById(Long id) {
        AdministratorProfile administratorProfile = this.administratorProfileRepository.findById(id)
                .orElseThrow(() -> new AdministratorNotFoundException("Administrator not found with id : " + id));
        return AdministratorProfileResponseDTO.builder()
                .id(administratorProfile.getId())
                .photo(administratorProfile.getPhoto())
                .type(administratorProfile.getType())
                .departmentId(administratorProfile.getDepartmentId())
                .build();
    }
}
