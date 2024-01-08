package com.project.coursewizard.dao;

import com.project.coursewizard.entity.Course;
import com.project.coursewizard.entity.Instructor;
import com.project.coursewizard.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorDAO extends JpaRepository<Instructor, Integer> {
    List<Instructor> findByUserName(String userName);

    @Query("Select i from Instructor i where i.department=:dept order by i.firstName")
    List<Instructor> findAllActiveInstructorsInDept(@Param("dept") String dept);

    @Query("Select i from Instructor i where i.department!=:dept order by i.firstName")
    List<Instructor> findAllActiveInstructorsNotInDept(@Param("dept") String dept);

    @Query("select i from Instructor i JOIN FETCH i.courses where i.id = :id")
    Instructor findCoursesByInstructorId(@Param("id") int id);

    List<Instructor> findByUserNameStartingWith(String prefix);
}
