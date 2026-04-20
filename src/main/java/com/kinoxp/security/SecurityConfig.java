package com.kinoxp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpClient;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        http                .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/html/**", "/css/**", "/js/**").permitAll()
                         .requestMatchers(HttpMethod.POST, "/kino/users/login").permitAll()
                          .requestMatchers(HttpMethod.GET, "/kino/movies/**").permitAll()
                         .requestMatchers(HttpMethod.POST, "/kino/movies/**").hasRole("ADMIN")
                          .requestMatchers(HttpMethod.PUT, "/kino/movies/**").hasRole("ADMIN")
                          .requestMatchers(HttpMethod.DELETE, "/kino/movies/**").hasRole("ADMIN")

                           .anyRequest().authenticated()
                )
          .formLogin(form -> form.disable())
                  .httpBasic(basic -> basic.disable())
                    .csrf(csrf -> csrf.disable());

                   return http.build();

    }         



    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("1234")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);

    }
}
