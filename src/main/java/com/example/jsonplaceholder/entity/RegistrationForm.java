package com.example.jsonplaceholder.entity;

import com.example.jsonplaceholder.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    private String login;
    private String password;
    private Role role;
}