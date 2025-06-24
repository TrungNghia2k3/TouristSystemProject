package com.ntn.tourism.controller.admin;

import com.ntn.tourism.service.UserService;
import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/tour-manager")
public class TourManagerController {

    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String displayDashboard(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "tour-manager/index";
    }

    @GetMapping("/displayTables")
    public String displayTables(Model model, Principal principal) {

        // Get all users
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        String username = principal.getName();
        model.addAttribute("username", username);

        model.addAttribute("user", new UserRegisteredDTO());
        return "tour-manager/tables";
    }

    @GetMapping("/displayCharts")
    public String displayCharts() {
        return "tour-manager/charts";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserRegisteredDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "tour-manager/tables"; // Quay lại nếu có lỗi
        }
        // Nếu id == null, tạo user mới; nếu không, cập nhật user cũ
        if (userDTO.getId() == null) {
            userService.save(userDTO);
        } else {
            userService.update(userDTO);
        }
        return "redirect:/tour-manager/displayTables";
    }
}
