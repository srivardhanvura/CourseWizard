package com.project.coursewizard.dao;

import com.project.coursewizard.entity.Course;
import com.project.coursewizard.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseDAO extends JpaRepository<Course, Integer> {
    @Query("select c from Course c where c.status = 'Active'")
    List<Course> findActiveCourses();

    @Query("select c from Course c where c.status = 'Active' and instructor.id = :id")
    List<Course> findActiveCoursesByInstructorId(@Param("id") int id);

    @Query("select c from Course c where c.status = 'Active' order by c.code")
    List<Course> findAllActiveCourses();

    @Query("Select c from Course c where c.status='Active' and c.department=:dept order by c.code")
    List<Course> findAllActiveCoursesInDept(@Param("dept") String dept);

    @Query("Select c from Course c where c.status='Active' and c.department!=:dept order by c.code")
    List<Course> findAllActiveCoursesNotInDept(@Param("dept") String dept);

    @Query("Select c from Course c JOIN FETCH c.reviews where c.id=:id")
    Course findCourseAndReviewsByCourseId(int id);

    @Query("Select c from Course c where c.status!='Active'")
    List<Course> getAllInactiveCourses();

    @Query("Select c from Course c where c.department=:dept order by c.code")
    List<Course> findAllCoursesInDept(@Param("dept") String dept);

    @Query("Select c from Course c where c.department!=:dept order by c.code")
    List<Course> findAllCoursesNotInDept(@Param("dept") String dept);

    @Query("Select c.code from Course c where c.department=:dept order by c.code")
    List<String> findAllCourseIDsInDept(@Param("dept") String dept);
}
