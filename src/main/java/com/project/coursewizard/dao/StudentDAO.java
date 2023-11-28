package com.project.coursewizard.dao;

import com.project.coursewizard.entity.Course;
import com.project.coursewizard.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentDAO extends JpaRepository<Student, Integer> {
    List<Student> findByUserName(String userName);

    @Query("select s from Student s JOIN FETCH s.courses where s.id = :id")
    Student findCoursesByStudentId(@Param("id") int id);

    List<Student> findByDepartment(String department);
}
