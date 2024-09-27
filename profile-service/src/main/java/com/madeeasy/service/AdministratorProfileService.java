package com.madeeasy.service;

import com.madeeasy.dto.request.AdministratorProfileRequestDTO;
import com.madeeasy.dto.response.AdministratorProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdministratorProfileService {
    AdministratorProfileResponseDTO createAdministratorProfile(MultipartFile file, AdministratorProfileRequestDTO administratorProfileRequestDTO) throws IOException;

    AdministratorProfileResponseDTO getPhotoById(Long id);
}
