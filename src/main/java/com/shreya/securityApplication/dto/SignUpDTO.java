package com.shreya.securityApplication.dto;

import com.shreya.securityApplication.entity.enums.Permission;
import com.shreya.securityApplication.entity.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDTO {
    private String name;
    private String email;
    private String password;

    // This is not ideal for production purpose.This is for demonstration only.We should not roles during sighnUp process as everyone will become admin.
    private Set<Role> roles;
    private Set<Permission> permissions;
}
