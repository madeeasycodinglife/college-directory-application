package com.madeeasy.controller;

import com.madeeasy.dto.request.DepartmentRequestDTO;
import com.madeeasy.dto.response.DepartmentResponseDTO;
import com.madeeasy.service.DepartmentService;
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
@RequestMapping(path = "/api/department")
@RequiredArgsConstructor
public class DepartmentServiceController {

    private final DepartmentService departmentService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createCourse(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        DepartmentResponseDTO createdDepartment = this.departmentService.createDepartment(departmentRequestDTO);
        if (createdDepartment.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createdDepartment);
        } else if (createdDepartment.getStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createdDepartment);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping(path = "/get-department-by-id/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
        Map<String, String> errors = ValidationUtils.validatePositiveInteger(id.intValue(), "id");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        DepartmentResponseDTO department = this.departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
}
