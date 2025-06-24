package com.ntn.tourism.controller;

import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegisteredDTO());
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") UserRegisteredDTO userDTO, Model model) {
        try {
            userService.save(userDTO);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // Quay lại form đăng ký với thông báo lỗi
        }
    }
}
