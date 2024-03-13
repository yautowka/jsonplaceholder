package com.example.jsonplaceholder.config;

import com.example.jsonplaceholder.filter.JwtFilter;
import com.example.jsonplaceholder.models.Audit;
import com.example.jsonplaceholder.repos.AuditsRepository;
import com.example.jsonplaceholder.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@EnableWebMvc
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class WebSecurityConfig {
    private static final String[] WHITE_LIST_URL = {
            "/api/auth/**",
            "/h2-console/**"
    };
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuditsRepository auditsRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(req ->
                        req.requestMatchers(antMatcher(WHITE_LIST_URL[0])).permitAll()
                                .requestMatchers(antMatcher(WHITE_LIST_URL[1])).permitAll()
                                .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    auditsRepository.save(Audit
                            .builder()
                            .time(LocalDateTime.now())
                            .url(request.getRequestURI())
                            .username(request.getRemoteUser())
                            .method(request.getMethod())
                            .access(false)
                            .params(request.getQueryString())
                            .build());
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access denied");
                })
//                .and().build();
                .and().headers().frameOptions().disable().and().build();
//                .build();
    }


}