package com.ntn.tourism.controller;

import com.ntn.tourism.dto.UserLoginDTO;
import com.ntn.tourism.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequestMapping("/login")
public class LoginController {

    UserDetailsService userDetailsService;

    @ModelAttribute("user")
    public UserLoginDTO userLoginDTO() {
        return new UserLoginDTO();
    }

    @GetMapping
    public String login() {
        return "login-2";
    }

    @PostMapping
    public void loginUser(@ModelAttribute("user") UserLoginDTO userLoginDTO) {
        userDetailsService.loadUserByUsername(userLoginDTO.getUsername());
    }
}
