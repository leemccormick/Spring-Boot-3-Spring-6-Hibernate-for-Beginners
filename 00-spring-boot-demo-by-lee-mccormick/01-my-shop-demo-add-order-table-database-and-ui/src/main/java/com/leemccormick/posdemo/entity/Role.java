package com.leemccormick.posdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private String userId;

    @Column(name = "role")
    private String role;
}
