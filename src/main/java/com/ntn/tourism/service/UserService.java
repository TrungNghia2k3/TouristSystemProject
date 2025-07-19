package com.ntn.tourism.service;

import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.model.User;

import java.util.List;

public interface UserService {

    User save(UserRegisteredDTO userDTO);

    User update(UserRegisteredDTO userDTO);

    List<User> findAll();
}
