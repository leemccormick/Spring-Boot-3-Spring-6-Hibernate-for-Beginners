package com.leemccormick.posdemo.dao.User;

import com.leemccormick.posdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> { }
