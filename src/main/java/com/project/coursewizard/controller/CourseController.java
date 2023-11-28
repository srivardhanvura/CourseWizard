package com.project.coursewizard.controller;
import com.project.coursewizard.dao.CourseDAO;
import com.project.coursewizard.dao.ReviewDAO;
import com.project.coursewizard.dao.StudentDAO;
import com.project.coursewizard.entity.Course;
import com.project.coursewizard.entity.Review;
import com.project.coursewizard.entity.Student;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/course")
public class CourseController {

    StudentDAO studentDAO;
    CourseDAO courseDAO;
    ReviewDAO reviewDAO;

    public CourseController(StudentDAO studentDAO, CourseDAO courseDAO, ReviewDAO reviewDAO){
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.reviewDAO = reviewDAO;
    }

    public Course getCourseFromId(int courseId){
        Optional<Course> optionalCourse = courseDAO.findById(courseId);
        Course course=null;
        if(optionalCourse.isPresent()){
            course = optionalCourse.get();
        }
        return course;
    }

    Student getStudentById(int studentId){
        Optional<Student> optionalStudent = studentDAO.findById(studentId);
        Student student=null;
        if(optionalStudent.isPresent()){
            student = optionalStudent.get();
        }
        return student;
    }

    @GetMapping("/{courseId}")
    public String showCourse(Model model, @PathVariable("courseId") String id){
        Course course = getCourseFromId(Integer.parseInt(id));
        List<Review> reviews = course.getReviews();
        float averageScore = 0;
        if(reviews.size() > 0) {
            for (Review review : course.getReviews()) {
                averageScore += review.getRating();
            }
            averageScore /= course.getReviews().size();
            model.addAttribute("averageScore", Math.floor(averageScore * 100) / 100);
        }
        model.addAttribute("course", course);
        return "course/course-detail";
    }

    @PostMapping("/drop")
    public String dropCourse(@Param("studentId") int studentId, @Param("courseId") int courseId){
        Student student=getStudentById(studentId);
        if(student==null){
            return "redirect:/student/home";
        }
        Course course=getCourseFromId(courseId);
        if(course==null){
            return "redirect:/student/home";
        }
        student.dropCourse(course);
        studentDAO.save(student);
        course.setCurrentStrength(course.getCurrentStrength() - 1);
        courseDAO.save(course);
        return "redirect:/student/home";
    }

    @PostMapping("/enrol")
    public String enrolCourse(@Param("studentId") int studentId, @Param("courseId") int courseId){
        Student student=getStudentById(studentId);
        if(student==null){
            return "redirect:/student/home";
        }
        Course course=getCourseFromId(courseId);
        if(course==null){
            return "redirect:/student/home";
        }
        student.addCourse(course);
        studentDAO.save(student);
        course.setCurrentStrength(course.getCurrentStrength() + 1);
        courseDAO.save(course);
        return "redirect:/student/home";
    }

    @PostMapping("/edit")
    public String editCourse(@ModelAttribute("course") Course course){
        courseDAO.save(course);
        return "redirect:/instructor/home";
    }
}
