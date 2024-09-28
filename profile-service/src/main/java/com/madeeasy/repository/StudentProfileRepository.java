package com.madeeasy.repository;

import com.madeeasy.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    List<StudentProfile> findByDepartmentId(Long id);

    List<StudentProfile> findByStartYearAndEndYear(int startYear, int endYear);
}
