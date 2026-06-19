package com.hsms.userservice.entity;

import com.hsms.userservice.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="users")
public class User {

    @Id
    private Long userId;
    private String name;
    private String email;
	
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
