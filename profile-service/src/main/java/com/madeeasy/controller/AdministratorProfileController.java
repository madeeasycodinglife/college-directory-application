package com.madeeasy.controller;

import com.madeeasy.dto.request.AdministratorProfileRequestDTO;
import com.madeeasy.dto.request.StudentProfileRequestDTO;
import com.madeeasy.dto.response.AdministratorProfileResponseDTO;
import com.madeeasy.dto.response.StudentProfileResponseDTO;
import com.madeeasy.service.AdministratorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/administrator-profile")
@RequiredArgsConstructor
public class AdministratorProfileController {
    private final AdministratorProfileService administratorProfileService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createAdministratorProfile(@RequestParam("file") MultipartFile file,
                                                  AdministratorProfileRequestDTO administratorProfileRequestDTO) throws IOException {
        AdministratorProfileResponseDTO administratorProfile = this.administratorProfileService.createAdministratorProfile(file, administratorProfileRequestDTO);

        // Prepare the response with the image and additional data
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(administratorProfile);
    }

    @GetMapping(path = "/get-photo-by-id/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        AdministratorProfileResponseDTO administratorProfileResponseDTO = this.administratorProfileService.getPhotoById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(administratorProfileResponseDTO.getType()))
                .body(new ByteArrayResource(administratorProfileResponseDTO.getPhoto()));
    }
}
