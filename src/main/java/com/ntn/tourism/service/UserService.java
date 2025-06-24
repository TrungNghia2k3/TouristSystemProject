package com.ntn.tourism.service;

import com.ntn.tourism.model.Role;
import com.ntn.tourism.model.RoleType;
import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.RoleRepository;
import com.ntn.tourism.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("Invalid username or password.");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole().name()))
        );
    }

    public User save(UserRegisteredDTO userDTO) {
        Role role = roleRepository.findByRole(RoleType.valueOf(userDTO.getRole()));

        if (role == null) {
            throw new IllegalArgumentException("Thís role was not found: " + userDTO.getRole());
        }

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    public User update(UserRegisteredDTO userDTO) {

        // Tìm user theo ID
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userDTO.getId()));

        // Cập nhật thông tin
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());

        // Kiểm tra nếu người dùng nhập mật khẩu mới thì mới cập nhật
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Cập nhật Role
        Role role = roleRepository.findByRole(RoleType.valueOf(userDTO.getRole()));
        if (role == null) {
            throw new IllegalArgumentException("This role was not found: " + userDTO.getRole());
        }
        user.setRole(role);

        // Lưu vào database
        return userRepository.save(user);
    }


}
