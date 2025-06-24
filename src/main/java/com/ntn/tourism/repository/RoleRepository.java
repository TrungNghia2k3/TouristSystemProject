package com.ntn.tourism.repository;

import com.ntn.tourism.model.Role;
import com.ntn.tourism.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRole(RoleType role);
}
