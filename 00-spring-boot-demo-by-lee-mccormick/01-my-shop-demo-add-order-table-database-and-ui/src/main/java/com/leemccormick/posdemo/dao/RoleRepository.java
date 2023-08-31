package com.leemccormick.posdemo.dao;

import com.leemccormick.posdemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> { }
