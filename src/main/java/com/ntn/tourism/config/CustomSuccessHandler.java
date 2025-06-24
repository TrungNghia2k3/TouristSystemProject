package com.ntn.tourism.config;

import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomSuccessHandler.class);
    @Autowired
    private UserRepository userRepository; // Service để lấy thông tin user từ DB

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String redirectUrl = null;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        HttpSession session = request.getSession(); // Lấy session

        // Lấy thông tin user từ DB
        String email = authentication.getName(); // Username trong Spring Security thường là email
        User user = userRepository.findByEmail(email); // Giả sử bạn có hàm này trong UserService

        if (user != null) {
            session.setAttribute("user", user); // Lưu toàn bộ thông tin user vào session
            session.setAttribute("username", user.getFullName()); // Lưu riêng tên người dùng nếu cần
            session.setAttribute("role", user.getRole().toString()); // Lưu role vào session

            log.info("Fullname and Email stored in session: {} and {} ", user.getFullName(), user.getEmail());
            log.info("Role stored in session: {}", user.getRole().getRole().toString());
        }

        // Xác định URL chuyển hướng theo vai trò
        for (GrantedAuthority grantedAuthority : authorities) {
            String role = grantedAuthority.getAuthority();

            if (role.equals("ROLE_USER")) {
                redirectUrl = "/user";
                break;
            } else if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin";
                break;
            } else if (role.equals("ROLE_TOUR_MANAGER")) {
                redirectUrl = "/tour-manager";
                break;
            } else if (role.equals("ROLE_HOTEL_MANAGER")) {
                redirectUrl = "/hotel-manager";
                break;
            }
        }

        if (redirectUrl == null) {
            throw new IllegalStateException("No valid role found for redirection.");
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}

