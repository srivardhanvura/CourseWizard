package com.project.coursewizard.controller;

import com.project.coursewizard.dao.CourseDAO;
import com.project.coursewizard.dao.InstructorDAO;
import com.project.coursewizard.dao.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

}
