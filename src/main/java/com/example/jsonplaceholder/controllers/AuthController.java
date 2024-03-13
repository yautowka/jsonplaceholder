package com.example.jsonplaceholder.controllers;

import com.example.jsonplaceholder.entity.AuthenticationResponse;
import com.example.jsonplaceholder.entity.LoginForm;
import com.example.jsonplaceholder.entity.RegistrationForm;
import com.example.jsonplaceholder.entity.SimpleResponse;
import com.example.jsonplaceholder.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    @PostMapping(path = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ResponseEntity<SimpleResponse> addNewUser(RegistrationForm form) {
        return ResponseEntity.ok(authService.signup(form));
    }

    @GetMapping(path = "/login")
    public @ResponseBody ResponseEntity<AuthenticationResponse> login(LoginForm form) {
        return ResponseEntity.ok(authService.login(form));
    }
}