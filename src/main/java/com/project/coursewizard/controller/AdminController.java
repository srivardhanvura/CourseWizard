package com.project.coursewizard.controller;

import com.project.coursewizard.dao.*;
import com.project.coursewizard.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        Course course;
        if(optionalCourse.isPresent()) {
            course = optionalCourse.get();
            course.setStatus("Inactive");
            courseDAO.save(course);
        }
        return "redirect:/admin/home";
    }

    @GetMapping("/deleteInstructor")
    public String deleteInstructor(Model model){
        model.addAttribute("userType", "instructor");
        return "admin/delete-user";
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(Model model){
        model.addAttribute("userType", "student");
        return "admin/delete-user";
    }

    @GetMapping("/studentFromUserName/{userName}")
    @ResponseBody
    public List<Student> findStudentFromUserName(@PathVariable("userName") String userName){
        List<Student> students = studentDAO.findByUserNameStartingWith(userName);
        if(students.size() == 0){
            return new ArrayList<>();
        }
        return students;
    }

    @GetMapping("/removeStudent/{id}")
    public String removeStudent(@PathVariable("id") int id){
        Student student = studentDAO.findById(id).orElseThrow();
        userDAO.delete(userDAO.findByUserName(student.getUserName()).get(0));
        studentDAO.delete(student);
        return "admin/admin-home";
    }

    @GetMapping("/instructorFromUserName/{userName}")
    @ResponseBody
    public List<Instructor> findInstructorFromUserName(@PathVariable("userName") String userName){
        List<Instructor> instructors = instructorDAO.findByUserNameStartingWith(userName);
        if(instructors.size() == 0){
            return new ArrayList<>();
        }
        return instructors;
    }

    @GetMapping("/removeInstructor/{id}")
    public String removeInstructor(@PathVariable("id") int id){
        Instructor instructor = instructorDAO.findById(id).orElseThrow();

        List<Course> courses = courseDAO.findAllCoursesByInstructorId(instructor.getId());
        for(Course course:courses){
            course.setStatus("Inactive");
            course.setInstructor(null);
        }
        User user = userDAO.findByUserName(instructor.getUserName()).get(0);

        courseDAO.saveAll(courses);
        userDAO.delete(user);
        instructorDAO.delete(instructor);
        
        return "admin/admin-home";
    }

    @GetMapping("/allCourses")
    public String getAllCourses(Model model){
        List<Course> courses = courseDAO.findAll(Sort.by(Sort.Direction.ASC, "code"));
        model.addAttribute("courses", courses);
        return "admin/all-courses";
    }

    @GetMapping("/allInstructors")
    public String getAllInstructors(Model model){
        List<Instructor> instructors = instructorDAO.findAll(Sort.by(Sort.Direction.ASC, "department"));
        model.addAttribute("instructors", instructors);
        return "admin/all-users";
    }

    @GetMapping("/allStudents")
    public String getAllStudents(Model model){
        List<Student> students = studentDAO.findAll(Sort.by(Sort.Direction.ASC, "department"));
        model.addAttribute("students", students);
        return "admin/all-users";
    }
}
