package com.example.chess.controller;

import com.example.chess.model.School;
import com.example.chess.model.User;
import org.springframework.stereotype.Controller;
import com.example.chess.Repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private SchoolRepository schoolRepository;

    @GetMapping("/index")
    public String home(Model model) {
        model.addAttribute("title", "Chesster");
        return "admin/index";
    }

    @GetMapping("/schools")
    public String schools(Model model) {
        model.addAttribute("title", "Schools");
        model.addAttribute("schools", schoolRepository.findAll());
        return "admin/schools";
    }

    @GetMapping("/school/{id}")
    public String viewSchool(@PathVariable Long id, Model model) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));
        model.addAttribute("school", school);
        return "admin/school";
    }

    @PostMapping("/schools/enable")
    public String enableSchool(@RequestParam("schoolId") Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));
        User user=school.getUser();
        user.setEnabled(true);
        schoolRepository.save(school);
        return "redirect:/admin/schools";
    }

    @PostMapping("/schools/disable")
    public String disableSchool(@RequestParam("schoolId") Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));
        User user=school.getUser();
        user.setEnabled(false);
        schoolRepository.save(school);
        return "redirect:/admin/schools";
    }

}
