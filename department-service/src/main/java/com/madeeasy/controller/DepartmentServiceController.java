package com.madeeasy.controller;

import com.madeeasy.dto.request.DepartmentRequestDTO;
import com.madeeasy.dto.response.DepartmentResponseDTO;
import com.madeeasy.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/department")
@RequiredArgsConstructor
public class DepartmentServiceController {

    private final DepartmentService departmentService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createCourse(@RequestBody DepartmentRequestDTO departmentRequestDTO) {
        DepartmentResponseDTO createdDepartment = this.departmentService.createDepartment(departmentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping(path = "/get-department-by-id/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO department = this.departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
}
