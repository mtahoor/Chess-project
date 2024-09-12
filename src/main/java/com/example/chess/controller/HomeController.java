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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        return "Student/signUp";
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
    @Transactional
    public String signupSchool(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String schoolName,
            @RequestParam("address") String address,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("logoUrl") MultipartFile logoFile,
            @RequestParam("proofOfIdentity") MultipartFile proofOfIdentityFile,
            Model model) {

        try {
            // Retrieve the role for school
            Role schoolRole = roleRepository.findByName("ROLE_SCHOOL")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            // Save the uploaded files
            String logoFileName = saveFile(logoFile);
            String proofOfIdentityFileName = saveFile(proofOfIdentityFile);

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
            school.setPhoneNumber(phoneNumber);
            school.setLogoUrl(logoFileName);
            school.setProofOfIdentity(proofOfIdentityFileName);
            school.setUser(user);
            schoolRepository.save(school);

            return "redirect:/login?signupSuccess";

        } catch (Exception e) {
            model.addAttribute("error", "Error occurred while signing up: " + e.getMessage());
            return "error/500";
        }

    }

    @PostMapping("/student_signup")
    @Transactional
    public String signupStudent(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("school") int schoolId,
            @RequestParam("name") String name,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("profile") MultipartFile profilePictureFile,
            RedirectAttributes redirectAttributes) {

        try {
            // Retrieve the role for student
            Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            String profilePicturePath = saveFile(profilePictureFile);

            User user = new User(
                    email,
                    username,
                    passwordEncoder.encode(password),
                    false, 
                    studentRole
            );
            userRepository.save(user);

            School school = schoolRepository.findBySchoolId(schoolId);

            Student student = new Student();
            student.setName(name);
            student.setPhoneNumber(phoneNumber);
            student.setProfilePicture(profilePicturePath);
            student.setUser(user);
            student.setSchool(school);
            student.setRating(0);
            studentRepository.save(student);

            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("Error occurred while signing up: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error occurred while signing up: " + e.getMessage());
            return "redirect:/student_signup";
        }
    }



    private String saveFile(MultipartFile file) {
    // Check if the file is not empty
    if (!file.isEmpty()) {
        try {
            // Generate a unique file name
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Define the path to save the file (you can create an "uploads" folder in your project directory)
            Path uploadPath = Paths.get("uploads");

            // Ensure the directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file to the directory
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    return "no file attached";
    }
}
