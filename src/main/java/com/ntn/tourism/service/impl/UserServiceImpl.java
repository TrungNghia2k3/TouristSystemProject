package com.ntn.tourism.service.impl;

import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.model.Role;
import com.ntn.tourism.model.RoleType;
import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.RoleRepository;
import com.ntn.tourism.repository.UserRepository;
import com.ntn.tourism.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Override
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

    @Override
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

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
