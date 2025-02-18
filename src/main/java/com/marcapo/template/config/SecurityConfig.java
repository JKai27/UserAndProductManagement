package com.marcapo.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf(csrf -> csrf.ignoringAntMatchers("/users/**", "/auth/login", "/auth/logout"))
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/users/**").permitAll()
                        .antMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated() //any other request than "/users" must be authenticated

                )
                .headers().frameOptions().disable();
        http.formLogin(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
