package com.madeeasy.controller;

import com.madeeasy.dto.request.UserPatchRequestDTO;
import com.madeeasy.dto.request.UserRequestDTO;
import com.madeeasy.dto.response.UserAuthResponseDTO;
import com.madeeasy.dto.response.UserResponseDTO;
import com.madeeasy.entity.Role;
import com.madeeasy.service.UserService;
import com.madeeasy.util.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO user) {
        UserAuthResponseDTO savedUser = this.userService.createUser(user);
        if (savedUser.getStatus() == HttpStatus.CONFLICT) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(savedUser);
        } else if (savedUser.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PatchMapping(path = "/partial-update/{emailId}")
    public ResponseEntity<?> partiallyUpdateUser(@PathVariable("emailId") String emailId,
                                                 @RequestBody UserPatchRequestDTO user) {
        Map<String, String> validationErrors = ValidationUtils.validateEmail(emailId);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
        }
        UserAuthResponseDTO updatedUser = this.userService.partiallyUpdateUser(emailId, user);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("status", HttpStatus.NOT_FOUND,
                            "message", "User not found with emailId: " + emailId)
            );
        }
        if (updatedUser.getStatus() == HttpStatus.CONFLICT) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(updatedUser);
        } else if (updatedUser.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updatedUser);
        } else if (updatedUser.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @GetMapping(path = "/{emailId}")
    public ResponseEntity<?> getUserByEmailId(@PathVariable("emailId") String emailId) {
        Map<String, String> validationErrors = ValidationUtils.validateEmail(emailId);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
        }
        UserResponseDTO user = this.userService.getUserByEmailId(emailId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with emailId: " + emailId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping(path = "/get-by-full-name-and-role/{fullName}/{role}")
    public ResponseEntity<?> findByFullNameAndRole(@PathVariable("fullName") String fullName, @PathVariable String role) {
        // Validate the role
        Map<String, String> roleValidationErrors = ValidationUtils.validateRole(role);
        if (!roleValidationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleValidationErrors);
        }

        // Proceed with finding the user by full name and role
        UserResponseDTO user = this.userService.findByFullNameAndRole(fullName, Role.valueOf(role.toUpperCase()));


        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with fullName: " + fullName);
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @GetMapping(path = "/get-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Map<String, String> validationErrors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
        }
        UserResponseDTO user = this.userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
