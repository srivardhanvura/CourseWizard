package com.project.coursewizard.controller;

import com.project.coursewizard.config.AuthenticationSuccessHandler;
import com.project.coursewizard.dao.InstructorDAO;
import com.project.coursewizard.dao.StudentDAO;
import com.project.coursewizard.entity.Instructor;
import com.project.coursewizard.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PublicController {

    InstructorDAO instructorDAO;
    StudentDAO studentDAO;

    @Autowired
    public PublicController(InstructorDAO instructorDAO, StudentDAO studentDAO){
        this.instructorDAO=instructorDAO;
        this.studentDAO=studentDAO;
    }

    @GetMapping("/")
    public String home(Authentication authentication, AuthenticationSuccessHandler authenticationSuccessHandler){
        if(authentication == null || !authentication.isAuthenticated()){
            return "redirect:/loginPage";
        }
        String home_url = authenticationSuccessHandler.determineTargetUrl(authentication);
        return "redirect:" + home_url;
    }

    @GetMapping("/loginPage")
    public String login(Authentication authentication, AuthenticationSuccessHandler authenticationSuccessHandler){
        if(authentication == null || !authentication.isAuthenticated()){
            return "home/login-page";
        }
        String home_url = authenticationSuccessHandler.determineTargetUrl(authentication);
        return "redirect:" + home_url;
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage(){
        return "home/access-denied";
    }

    @GetMapping("/instructors")
    public String showInstructors(Model model, AuthenticationSuccessHandler successHandler){
        List<Instructor> instructors = instructorDAO.findAll();
        model.addAttribute("data", instructors);
        model.addAttribute("name", "instructors");
        model.addAttribute("role", successHandler.getCurrentRole());
        return "fragments/list";
    }

    @GetMapping("/custom-logout")
    public String logout() {
        return "redirect:/logout";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, AuthenticationSuccessHandler successHandler){
        String loggedInUserName = successHandler.getCurrentUsername();
        String role = successHandler.getCurrentRole();
        if(role.equals("student")) {
            Student student = studentDAO.findByUserName(loggedInUserName).get(0);
            model.addAttribute("user", student);
        } else if (role.equals("instructor")) {
            Instructor instructor = instructorDAO.findByUserName(loggedInUserName).get(0);
            model.addAttribute("user", instructor);
        }
        return "fragments/profile";
    }
}
