package com.project.coursewizard.controller;

import com.project.coursewizard.config.AuthenticationSuccessHandler;
import com.project.coursewizard.dao.CourseDAO;
import com.project.coursewizard.dao.InstructorDAO;
import com.project.coursewizard.dao.StudentDAO;
import com.project.coursewizard.entity.Course;
import com.project.coursewizard.entity.Instructor;
import com.project.coursewizard.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    private InstructorDAO instructorDAO;

    @Autowired
    public StudentController(StudentDAO studentDAO, CourseDAO courseDAO, InstructorDAO instructorDAO){
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.instructorDAO = instructorDAO;
    }

    @GetMapping("/")
    public String studentDef(){
        return "redirect:/student/home";
    }

    @GetMapping("/home")
    public String home(Model model, AuthenticationSuccessHandler authenticationSuccessHandler){
        Student student = studentDAO.findByUserName(authenticationSuccessHandler.getCurrentUsername()).get(0);
        Student studentWithCourses = studentDAO.findCoursesByStudentId(student.getId());
        if(studentWithCourses == null){
            student.setCourses(new ArrayList<>());
        }
        else {
            student.setCourses(studentWithCourses.getCourses());
        }
        int totalCredits = 0;
        for(Course course:student.getCourses()){
            totalCredits += course.getCredits();
        }
        model.addAttribute("student", student);
        model.addAttribute("totalCredits", totalCredits);
        return "student/student-home";
    }

    Student getStudentById(int studentId){
        Optional<Student> optionalStudent = studentDAO.findById(studentId);
        Student student=null;
        if(optionalStudent.isPresent()){
            student = optionalStudent.get();
        }
        return student;
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

    @GetMapping("/dropCourse")
    public String dropCourse(Model model, @Param("studentId") int studentId, @Param("courseId") int courseId, AuthenticationSuccessHandler successHandler){
        String loggedInUserName = successHandler.getCurrentUsername();
        Student student = getStudentById(studentId);
        if(student == null) {
            return errorPage(model, "Invalid request","User with id " + studentId + " not found.");
        }
        if(!loggedInUserName.equals(student.getUserName())){
            return errorPage(model, "Invalid request","User with id " + studentId + " not found.");
        }

        Student studentWithCourses = studentDAO.findCoursesByStudentId(studentId);
        if(studentWithCourses == null){
            student.setCourses(new ArrayList<>());
        }
        else {
            student.setCourses(studentWithCourses.getCourses());
        }
        boolean courseFound = false;
        List<Course> courses = student.getCourses();
        for (Course cours : courses) {
            if (cours.getId() == courseId) {
                courseFound = true;
                break;
            }
        }
        if(!courseFound){
            return errorPage(model, "Invalid request","Course with id: " + courseId + " not found for this user: " + student.getUserName());
        }

        Course course = getCourseById(courseId);
        if(course == null){
            return errorPage(model, "Invalid request","Course not found.");
        }
        model.addAttribute("student", student);
        model.addAttribute("course", course);
        model.addAttribute("type", "drop");
        return "student/enrol-drop-course";
    }

    @GetMapping("/enrolCourse")
    public String enrolCourse(Model model, @Param("studentId") int studentId, @Param("courseId") int courseId, AuthenticationSuccessHandler successHandler){
        String loggedInUserName = successHandler.getCurrentUsername();
        Student student = getStudentById(studentId);
        if(student == null) {
            return errorPage(model, "Invalid request","User with id " + studentId + " not found.");
        }
        if(!loggedInUserName.equals(student.getUserName())){
            return errorPage(model, "Invalid request","User with id " + studentId + " not found.");
        }

        Course course = getCourseById(courseId);
        if(course == null){
            return errorPage(model, "Invalid request","Course not found.");
        }
        model.addAttribute("student", student);
        model.addAttribute("course", course);
        model.addAttribute("type", "enrol");
        return "student/enrol-drop-course";
    }

    @GetMapping("/allCourses")
    public String showAllCourses(Model model, AuthenticationSuccessHandler successHandler){
        Student student = studentDAO.findByUserName(successHandler.getCurrentUsername()).get(0);
        Student studentWithCourses = studentDAO.findCoursesByStudentId(student.getId());
        if(studentWithCourses == null){
            student.setCourses(new ArrayList<>());
        }
        else {
            student.setCourses(studentWithCourses.getCourses());
        }
        List<Course> deptCourses = courseDAO.findAllActiveCoursesInDept(student.getDepartment());
        List<Course> studentCourses = student.getCourses();
        for(Course course: studentCourses){
            deptCourses.remove(course);
        }

        List<Course> nonDeptCourses = courseDAO.findAllActiveCoursesNotInDept(student.getDepartment());
        List<Course> inactiveCourses = courseDAO.getAllInactiveCourses();

        model.addAttribute("student", student);
        model.addAttribute("deptCourses", deptCourses);
        model.addAttribute("nonDeptCourses", nonDeptCourses);
        model.addAttribute("inactiveCourses", inactiveCourses);
        return "student/all-courses";
    }

    @GetMapping("/allInstructors")
    public String showAllInstructors(Model model, AuthenticationSuccessHandler authenticationSuccessHandler){
        Student student = studentDAO.findByUserName(authenticationSuccessHandler.getCurrentUsername()).get(0);
        List<Instructor> deptInstructors = instructorDAO.findAllActiveInstructorsInDept(student.getDepartment());
        List<Instructor> nonDeptInstructors = instructorDAO.findAllActiveInstructorsNotInDept(student.getDepartment());
        model.addAttribute("deptInstructors", deptInstructors);
        model.addAttribute("nonDeptInstructors", nonDeptInstructors);
        model.addAttribute("userType", "student");
        return "home/instructors-list";
    }
}
