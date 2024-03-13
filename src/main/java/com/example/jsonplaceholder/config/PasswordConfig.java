package com.example.jsonplaceholder.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/*
Chipi chipi chapa chapa
Dubi dubi daba daba
Mágico mi dubi dubi
Bum, bum, bum, bum
Chipi chipi chapa chapa
Dubi dubi daba daba
Mágico mi dubi dubi
Bum
*/

@Configuration
@RequiredArgsConstructor
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
