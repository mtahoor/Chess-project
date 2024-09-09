package com.example.chess.controller;

import com.example.chess.Repository.SchoolRepository;
import com.example.chess.Repository.StudentRepository;
import com.example.chess.Repository.UserRepository;
import com.example.chess.model.School;
import com.example.chess.model.Student;
import com.example.chess.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/school")
public class SchoolController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;


    public SchoolController(AuthenticationManager authenticationManager, UserRepository userRepository, SchoolRepository schoolRepository, StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/index")
    public String home(Model model) {
        model.addAttribute("title", "Chesster");
        return "school/index";
    }

    @GetMapping("/students")
    public String schools(Model model, Authentication authentication) {
        model.addAttribute("title", "Schools");
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        School school = schoolRepository.findByUser(user);
        List<Student> students = studentRepository.findBySchool(school);
        model.addAttribute("students", students);
        return "school/students";
    }

    @GetMapping("/student/{id}")
    public String viewSchool(@PathVariable Long id, Model model) {
        System.out.println(id);
        Student student = studentRepository.findByStudentId(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        return "school/student";
    }

    @PostMapping("/student/enable")
    public String enableStudent(@RequestParam("studentId") Long StudentId) {
        Student student = studentRepository.findByStudentId(StudentId)
                .orElseThrow(() -> new RuntimeException("School not found"));
        User user=student.getUser();
        user.setEnabled(true);
        studentRepository.save(student);
        return "redirect:/school/students";
    }

    @PostMapping("/student/disable")
    public String disableStudent(@RequestParam("studentId") Long StudentId) {
        Student student = studentRepository.findByStudentId(StudentId)
                .orElseThrow(() -> new RuntimeException("School not found"));
        User user=student.getUser();
        user.setEnabled(false);
        studentRepository.save(student);
        return "redirect:/school/students";
    }


}
