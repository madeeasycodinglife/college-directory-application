package com.madeeasy.repository;

import com.madeeasy.entity.FacultyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyProfileRepository extends JpaRepository<FacultyProfile, Long> {

    List<FacultyProfile> findByDepartmentId(Long id);
}
