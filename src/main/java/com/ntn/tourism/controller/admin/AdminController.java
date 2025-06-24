package com.ntn.tourism.controller.admin;

import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.UserRepository;
import com.ntn.tourism.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String displayDashboard() {
        return "admin/index";
    }

    @GetMapping("/displayTables")
    public String displayTables(Model model) {

        // Get all users
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "admin/tables";
    }

    @GetMapping("/displayCharts")
    public String displayCharts() {
        return "admin/charts";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserRegisteredDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/tables"; // Quay lại nếu có lỗi
        }
        // Nếu id == null, tạo user mới; nếu không, cập nhật user cũ
        if (userDTO.getId() == null) {
            userService.save(userDTO);
        } else {
            userService.update(userDTO);
        }
        return "redirect:/admin/displayTables";
    }
}
