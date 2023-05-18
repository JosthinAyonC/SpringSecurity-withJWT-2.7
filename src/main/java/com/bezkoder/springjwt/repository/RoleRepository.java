package com.bezkoder.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.springjwt.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
