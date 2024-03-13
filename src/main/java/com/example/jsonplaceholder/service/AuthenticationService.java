package com.example.jsonplaceholder.service;

import com.example.jsonplaceholder.models.Role;
import com.example.jsonplaceholder.entity.AuthenticationResponse;
import com.example.jsonplaceholder.entity.LoginForm;
import com.example.jsonplaceholder.models.User;
import com.example.jsonplaceholder.entity.RegistrationForm;
import com.example.jsonplaceholder.entity.SimpleResponse;
import com.example.jsonplaceholder.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public SimpleResponse signup(RegistrationForm form) {
        var user = User.builder()
                .login(form.getLogin())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(form.getRole())
                .build();
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("User created")
                .build();

    }

    public AuthenticationResponse login(LoginForm form) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getLogin(),
                        form.getPassword()));
        var user = userRepository.findByLogin(form.getLogin())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
