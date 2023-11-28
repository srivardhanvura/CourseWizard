package com.project.coursewizard.controller;

import com.project.coursewizard.config.AuthenticationSuccessHandler;
import com.project.coursewizard.dao.CourseDAO;
import com.project.coursewizard.dao.InstructorDAO;
import com.project.coursewizard.dao.StudentDAO;
import com.project.coursewizard.entity.Course;
import com.project.coursewizard.entity.Instructor;
import com.project.coursewizard.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    InstructorDAO instructorDAO;
    CourseDAO courseDAO;
    StudentDAO studentDAO;

    @Autowired
    InstructorController(InstructorDAO instructorDAO, CourseDAO courseDAO, StudentDAO studentDAO){
        this.instructorDAO = instructorDAO;
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
    }

    Course getCourseById(int courseId){
        Optional<Course> optionalCourse = courseDAO.findById(courseId);
        Course course=null;
        if(optionalCourse.isPresent()){
            course = optionalCourse.get();
        }
        return course;
    }

    public String errorPage(Model model, String error, String message){
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        return "fragments/forbidden-page";
    }

    @GetMapping("/")
    public String studentDef(){
        return "redirect:/instructor/home";
    }


    @GetMapping("/home")
    public String showHome(Model model, AuthenticationSuccessHandler successHandler){
        Instructor instructor = instructorDAO.findByUserName(successHandler.getCurrentUsername()).get(0);
        Instructor instructorWithCourses = instructorDAO.findCoursesByInstructorId(instructor.getId());
        if(instructorWithCourses == null){
            instructor.setCourses(new ArrayList<>());
        }
        else {
            instructor.setCourses(instructorWithCourses.getCourses());
        }
        model.addAttribute("instructor", instructor);
        return "instructor/instructor-home";
    }

    @GetMapping("/editCourse")
    public String editCourse(Model model, @Param("courseId") int courseId){
        Course course = getCourseById(courseId);
        if(course == null){
            return errorPage(model, "Invalid request","Course not found.");
        }
        model.addAttribute("course", course);
        return "instructor/edit-course";
    }

    @GetMapping("/allCourses")
    public String showAllCourses(Model model, AuthenticationSuccessHandler successHandler){
        Instructor instructor = instructorDAO.findByUserName(successHandler.getCurrentUsername()).get(0);
        List<Course> deptCourses = courseDAO.findAllCoursesInDept(instructor.getDepartment());
        List<Course> nonDeptCourses = courseDAO.findAllCoursesNotInDept(instructor.getDepartment());
        model.addAttribute("fullName", instructor.getFirstName() + ' ' + instructor.getLastName());
        model.addAttribute("deptCourses", deptCourses);
        model.addAttribute("nonDeptCourses", nonDeptCourses);
        return "instructor/all-courses";
    }

    @GetMapping("/allInstructors")
    public String showAllInstructors(Model model, AuthenticationSuccessHandler successHandler){
        Instructor instructor = instructorDAO.findByUserName(successHandler.getCurrentUsername()).get(0);
        List<Instructor> deptInstructors = instructorDAO.findAllActiveInstructorsInDept(instructor.getDepartment());
        List<Instructor> nonDeptInstructors = instructorDAO.findAllActiveInstructorsNotInDept(instructor.getDepartment());
        model.addAttribute("deptInstructors", deptInstructors);
        model.addAttribute("nonDeptInstructors", nonDeptInstructors);
        model.addAttribute("userType", "instructor");
        return "home/instructors-list";
    }

    @GetMapping("/allStudents")
    public String showAllStudents(Model model, AuthenticationSuccessHandler successHandler){
        Instructor instructor = instructorDAO.findByUserName(successHandler.getCurrentUsername()).get(0);
        List<Student> students = studentDAO.findByDepartment(instructor.getDepartment());
        model.addAttribute("students", students);
        model.addAttribute("userType", "instructor");
        return "instructor/all-students";
    }
}
