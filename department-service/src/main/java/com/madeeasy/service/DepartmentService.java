package com.madeeasy.service;

import com.madeeasy.dto.request.DepartmentRequestDTO;
import com.madeeasy.dto.response.DepartmentResponseDTO;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentService);

    DepartmentResponseDTO getDepartmentById(Long id);
}
