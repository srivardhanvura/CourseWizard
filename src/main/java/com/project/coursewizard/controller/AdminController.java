package com.project.coursewizard.controller;

import com.project.coursewizard.dao.CourseDAO;
import com.project.coursewizard.dao.InstructorDAO;
import com.project.coursewizard.dao.StudentDAO;
import com.project.coursewizard.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    InstructorDAO instructorDAO;
    CourseDAO courseDAO;
    StudentDAO studentDAO;

    @Autowired
    AdminController(InstructorDAO instructorDAO, CourseDAO courseDAO, StudentDAO studentDAO){
        this.instructorDAO = instructorDAO;
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
    }

    @GetMapping("/")
    public String systemShowHome(){
        return "redirect:/admin/home";
    }

    @GetMapping("/home")
    public String systemHomePage(){
        return "admin/admin-home";
    }

    @GetMapping("/addStudent")
    public String addStudent(Model model){
        Student student = new Student();
        model.addAttribute("userType", "student");
        model.addAttribute("student", student);
        return "admin/add-user";
    }

    @GetMapping("/addInstructor")
    public String addInstructor(){
        return null;
    }

    @GetMapping("/addCourse")
    public String addCourse(){
        return null;
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(){
        return null;
    }

    @GetMapping("/deleteInstructor")
    public String deleteInstructor(){
        return null;
    }

    @GetMapping("/deleteCourse")
    public String deleteCourse(){
        return null;
    }

    @GetMapping("/allCourses")
    public String getAllCourses(){
        return null;
    }

    @GetMapping("/allInstructors")
    public String getAllInstructors(){
        return null;
    }

    @GetMapping("/allStudents")
    public String getAllStudents(){
        return null;
    }
}
