package com.madeeasy.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeeasy.dto.request.AdministratorPartialProfileRequestDTO;
import com.madeeasy.dto.request.AdministratorProfileRequestDTO;
import com.madeeasy.dto.response.AdministratorProfileResponseDTO;
import com.madeeasy.entity.AdministratorProfile;
import com.madeeasy.exception.AdministratorNotFoundException;
import com.madeeasy.repository.AdministratorProfileRepository;
import com.madeeasy.service.AdministratorProfileService;
import com.madeeasy.vo.DepartmentResponseDTO;
import com.madeeasy.vo.UserResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdministratorProfileServiceImpl implements AdministratorProfileService {
    private final AdministratorProfileRepository administratorProfileRepository;
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "createAdministratorProfileFallbackMethod")
    @Override
    public AdministratorProfileResponseDTO createAdministratorProfile(MultipartFile file, AdministratorProfileRequestDTO administratorProfileRequestDTO) throws IOException {

        String departmentUrl = "http://department-service/api/department/get-department-by-id/" + administratorProfileRequestDTO.getDepartmentId();

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + authToken);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);

        DepartmentResponseDTO response =
                restTemplate.exchange(departmentUrl, HttpMethod.GET, null, DepartmentResponseDTO.class)
                        .getBody();

        String userUrl = "http://user-service/api/user/get-by-id/" + administratorProfileRequestDTO.getId();


        UserResponseDTO userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, null, UserResponseDTO.class).getBody();


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


    public AdministratorProfileResponseDTO createAdministratorProfileFallbackMethod(MultipartFile file,
                                                                                    AdministratorProfileRequestDTO administratorProfileRequestDTO,
                                                                                    Throwable t) {


        log.error("message : {}", t.getMessage());

        // Check if the throwable is an instance of HttpClientErrorException
        if (t instanceof HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                try {
                    // Parse the response body as JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(exception.getResponseBodyAsString());

                    // Extract specific fields from the JSON, such as 'message' and 'status'
                    String errorMessage = jsonNode.path("message").asText();
                    String errorStatus = jsonNode.path("status").asText();

                    // Log the extracted information
                    log.error("message : {} , status : {}", errorMessage, errorStatus);

                    return AdministratorProfileResponseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMessage)
                            .build();
                } catch (Exception e) {
                    log.error("Failed to parse the error response", e);
                }
            } else {
                try {
                    // Parse the response body as JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(exception.getResponseBodyAsString());

                    // Extract specific fields from the JSON, such as 'message' and 'status'
                    String errorMessage = jsonNode.path("message").asText();
                    String errorStatus = jsonNode.path("status").asText();

                    // Log the extracted information
                    log.error("message : {} , status : {}", errorMessage, errorStatus);

                    return AdministratorProfileResponseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMessage)
                            .build();
                } catch (Exception e) {
                    log.error("Failed to parse the error response", e);
                }
            }
        }

        // Fallback response if the exception is not HttpClientErrorException or any other case
        return AdministratorProfileResponseDTO.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("Sorry !! Service is unavailable. Please try again later.")
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

    @Override
    public AdministratorProfileResponseDTO getAdministratorById(Long id) {

        AdministratorProfile administratorProfile = this.administratorProfileRepository.findById(id)
                .orElseThrow(() -> new AdministratorNotFoundException("Administrator not found with id : " + id));
        return AdministratorProfileResponseDTO.builder()
                .id(administratorProfile.getId())
                .photo(administratorProfile.getPhoto())
                .type(administratorProfile.getType())
                .departmentId(administratorProfile.getDepartmentId())
                .build();
    }

    @Override
    public AdministratorProfileResponseDTO partiallyUpdateUser(Long id, AdministratorPartialProfileRequestDTO administratorProfileRequestDTO) {

        AdministratorProfile administratorProfile = this.administratorProfileRepository.findById(id)
                .orElseThrow(() -> new AdministratorNotFoundException("Administrator not found with id : " + id));


        if (administratorProfileRequestDTO.getDepartmentId() != null) {
            administratorProfile.setDepartmentId(administratorProfileRequestDTO.getDepartmentId());
        }

        this.administratorProfileRepository.save(administratorProfile);

        return AdministratorProfileResponseDTO.builder()
                .id(administratorProfile.getId())
                .photo(administratorProfile.getPhoto())
                .type(administratorProfile.getType())
                .departmentId(administratorProfile.getDepartmentId())
                .build();
    }
}
