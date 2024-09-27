package com.madeeasy.service.impl;

import com.madeeasy.dto.request.DepartmentRequestDTO;
import com.madeeasy.dto.response.DepartmentResponseDTO;
import com.madeeasy.entity.Department;
import com.madeeasy.exception.DepartmentNotFoundException;
import com.madeeasy.repository.DepartmentRepository;
import com.madeeasy.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentService) {
        Department department = Department.builder()
                .name(departmentService.getName())
                .description(departmentService.getDescription())
                .build();
        Department savedDepartment = this.departmentRepository.save(department);
        return DepartmentResponseDTO.builder()
                .id(savedDepartment.getId())
                .name(savedDepartment.getName())
                .description(savedDepartment.getDescription())
                .build();
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = this.departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id : " + id));
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
