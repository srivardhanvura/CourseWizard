package com.project.coursewizard.controller;

import com.project.coursewizard.dao.*;
import com.project.coursewizard.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    InstructorDAO instructorDAO;
    CourseDAO courseDAO;
    StudentDAO studentDAO;
    DepartmentDAO departmentDAO;
    UserDAO userDAO;

    @Autowired
    AdminController(InstructorDAO instructorDAO, CourseDAO courseDAO, StudentDAO studentDAO, DepartmentDAO departmentDAO, UserDAO userDAO){
        this.instructorDAO = instructorDAO;
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
        this.departmentDAO = departmentDAO;
        this.userDAO = userDAO;
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
        List<String> codes = new ArrayList<>();
        List<Department> departments = departmentDAO.findAll();
        for(Department department:departments){
            codes.add(department.getCode());
        }
        model.addAttribute("departments", codes);
        return "admin/add-user";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student){
        String userName = student.getFirstName().toLowerCase() + "_" + student.getLastName().toLowerCase();
        student.setUserName(userName);
        User user = new User(userName, "{noop}pass", 1);

        studentDAO.save(student);
        userDAO.save(user);
        return "redirect:/admin/home";
    }

    @GetMapping("/addInstructor")
    public String addInstructor(Model model){
        Instructor instructor = new Instructor();
        model.addAttribute("userType", "instructor");
        model.addAttribute("instructor", instructor);
        List<String> codes = new ArrayList<>();
        List<Department> departments = departmentDAO.findAll();
        for(Department department:departments){
            codes.add(department.getCode());
        }
        model.addAttribute("departments", codes);
        return "admin/add-user";
    }

    @PostMapping("/saveInstructor")
    public String saveStudent(@ModelAttribute("instructor") Instructor instructor){
        String userName = instructor.getFirstName().toLowerCase() + "_" + instructor.getLastName().toLowerCase();
        instructor.setUserName(userName);
        User user = new User(userName, "{noop}pass", 1);

        instructorDAO.save(instructor);
        userDAO.save(user);
        return "redirect:/admin/home";
    }

    @GetMapping("/addCourse")
    public String addCourse(Model model){
        List<Department> departments = departmentDAO.findAll();
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departments);
        return "admin/add-course";
    }

    @GetMapping("/getInstructors/{departmentId}")
    @ResponseBody
    public List<Instructor> getInstructorsByDepartment(@PathVariable("departmentId") String departmentId) {
        return instructorDAO.findAllActiveInstructorsInDept(departmentId);
    }

    @PostMapping("/saveCourse")
    public String saveCourse(@ModelAttribute("course") Course course, Model model) {
        String courseCode = course.getCode();
        String dept = course.getDepartment();
        List<String> courseIDs = courseDAO.findAllCourseIDsInDept(dept);
        for(String code:courseIDs){
            if(code.equalsIgnoreCase(dept + " " + courseCode)){
                List<Department> departments = departmentDAO.findAll();
                model.addAttribute("departments", departments);
                model.addAttribute("displayMainDiv", true);
                model.addAttribute("error", "Course code already assigned to another course.");
                model.addAttribute("course", course);
                return "admin/add-course";
            }
        }
        course.setCurrentStrength(0);
        course.setCode(dept + " " + courseCode);
        courseDAO.save(course);
        return "redirect:/admin/home";
    }

    @GetMapping("/getInstructorCourses/{instId}")
    @ResponseBody
    public List<Course> getInstructorCourses(@PathVariable("instId") int instId) {
        return courseDAO.findActiveCoursesByInstructorId(instId);
    }

    @GetMapping("/deleteCourse")
    public String deleteCourse(Model model){
        List<Department> departments = departmentDAO.findAll();
        model.addAttribute("departments", departments);
        return "admin/delete-course";
    }

    @GetMapping("/removeCourse/{courseId}")
    public String removeCourse(@PathVariable("courseId") int courseId){
        Optional<Course> optionalCourse = courseDAO.findById(courseId);
        Course course = null;
        if(optionalCourse.isPresent()){
            course = optionalCourse.get();
        }
        course.setStatus("Inactive");
        courseDAO.save(course);
        return "redirect:/admin/home";
    }

    @GetMapping("/deleteInstructor")
    public String deleteInstructor(){
        return null;
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(){
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
