package com.example.chess.controller;
import com.example.chess.Repository.RoleRepository;
import com.example.chess.Repository.SchoolRepository;
import com.example.chess.Repository.StudentRepository;
import com.example.chess.Repository.UserRepository;
import com.example.chess.model.Student;
import com.example.chess.model.Role;
import com.example.chess.model.School;
import com.example.chess.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder
    private final StudentRepository studentRepository;

    public HomeController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, SchoolRepository schoolRepository, PasswordEncoder passwordEncoder, StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Chesster");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }


    @GetMapping("/school_signup")
    public String SchoolSignup() {
        return "school/signUp";
    }

    @GetMapping("/student_signup")
    public String StudentSignup(Model model) {
        List<School> activeSchools = schoolRepository.findAll();
        model.addAttribute("schools", activeSchools);
        return "student/signUp";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_ADMIN")) {
                    return "redirect:/admin/index";
                } else if (role.equals("ROLE_SCHOOL")) {
                    return "redirect:/";
                } else if (role.equals("ROLE_STUDNET")) {
                    return "redirect:/student/dashboard";
                }
            }
            return "redirect:/login?error";

        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @PostMapping("/school_signup")
    public String signupSchool(@RequestParam("email") String email,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("name") String schoolName,
                               @RequestParam("address") String address) {

        // Retrieve the role for school
        Role schoolRole = roleRepository.findByName("ROLE_SCHOOL")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Log the incoming data
        System.out.println(schoolRole.getName());
        System.out.println(email);
        System.out.println(password);
        System.out.println(schoolName);
        System.out.println(username);

        // Create User
        User user = new User(
                email,
                username,
                passwordEncoder.encode(password),
                false, // Disabled until approved by the admin
                schoolRole
        );
        userRepository.save(user);

        // Create School
        School school = new School();
        school.setName(schoolName);
        school.setAddress(address);
        school.setUser(user);
        schoolRepository.save(school);

        return "redirect:/login?signupSuccess";
    }


    @PostMapping("/student_signup")
    public String signupStudent(@RequestParam("email") String email,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("school") int schoolId,
                               @RequestParam("name") String name) {

        // Retrieve the role for school
        Role schoolRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Log the incoming data
        System.out.println(schoolRole.getName());
        System.out.println(email);
        System.out.println(password);
        System.out.println(schoolId);
        System.out.println(username);

        // Create User
        User user = new User(
                email,
                username,
                passwordEncoder.encode(password),
                false, // Disabled until approved by the admin
                schoolRole
        );
        userRepository.save(user);
        School school = schoolRepository.findBySchoolId(schoolId);
        // create Student
        Student student = new Student();
        student.setName(name);
        student.setUser(user);
        student.setSchool(school);
        student.setRating(0);
        studentRepository.save(student);

        return "redirect:/login?signupSuccess";
    }


}
