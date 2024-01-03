package com.project.coursewizard.dao;

import com.project.coursewizard.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentDAO extends JpaRepository<Department, String> {
}
