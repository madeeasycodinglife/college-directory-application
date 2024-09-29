package com.madeeasy.controller;

import com.madeeasy.dto.request.AdministratorPartialProfileRequestDTO;
import com.madeeasy.dto.request.AdministratorProfileRequestDTO;
import com.madeeasy.dto.response.AdministratorProfileResponseDTO;
import com.madeeasy.service.AdministratorProfileService;
import com.madeeasy.util.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Validated
@RestController
@RequestMapping(path = "/api/administrator-profile")
@RequiredArgsConstructor
public class AdministratorProfileController {
    private final AdministratorProfileService administratorProfileService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createAdministratorProfile(@RequestParam("file") MultipartFile file,
                                                        @Valid AdministratorProfileRequestDTO administratorProfileRequestDTO) throws IOException {
        AdministratorProfileResponseDTO administratorProfile = this.administratorProfileService.createAdministratorProfile(file, administratorProfileRequestDTO);

        if (administratorProfile.getStatus() == HttpStatus.CONFLICT) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(administratorProfile);
        } else if (administratorProfile.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(administratorProfile);
        } else if (administratorProfile.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(administratorProfile);
        } else if (administratorProfile.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(administratorProfile);
        }
        // Prepare the response with the image and additional data
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(administratorProfile);
    }


    @PatchMapping(path = "/partial-update/{id}")
    public ResponseEntity<?> partiallyUpdateUser(@PathVariable("id") Long id,
                                                 @Valid @RequestBody AdministratorPartialProfileRequestDTO administratorProfileRequestDTO) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        AdministratorProfileResponseDTO administratorProfileResponseDTO = this.administratorProfileService.partiallyUpdateUser(id, administratorProfileRequestDTO);
        return ResponseEntity.ok()
                .body(administratorProfileResponseDTO);
    }

    @GetMapping(path = "/get-photo-by-id/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        AdministratorProfileResponseDTO administratorProfileResponseDTO = this.administratorProfileService.getPhotoById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(administratorProfileResponseDTO.getType()))
                .body(new ByteArrayResource(administratorProfileResponseDTO.getPhoto()));
    }

    @GetMapping(path = "/get-by-id/{id}")
    public ResponseEntity<?> getAdministratorById(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        AdministratorProfileResponseDTO administratorProfileResponseDTO = this.administratorProfileService.getAdministratorById(id);
        return ResponseEntity.ok()
                .body(administratorProfileResponseDTO);
    }
}
