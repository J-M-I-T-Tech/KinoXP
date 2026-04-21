package com.kinoxp.security;

import com.kinoxp.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http                .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/html/**", "/css/**", "/js/**").permitAll()
                         .requestMatchers(HttpMethod.POST, "/kino/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/kino/users").permitAll()                          .requestMatchers(HttpMethod.GET, "/kino/movies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/kino/movies/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/kino/movies/**").hasRole("ADMIN")
                          .requestMatchers(HttpMethod.PUT, "/kino/movies/**").hasRole("ADMIN")
                          .requestMatchers(HttpMethod.DELETE, "/kino/movies/**").hasRole("ADMIN")

                           .anyRequest().authenticated()
                )

                .formLogin( form -> form
                        .loginPage("/html/login.html")
                        .loginProcessingUrl("/kino/users/login")
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()

                )

                .logout(logout -> logout
                  .logoutUrl("/kino/users/logout")
                        .logoutSuccessUrl("/html/login.html")
                        .permitAll()


                )
                    .csrf(csrf ->csrf.disable ()); 



//          .formLogin(form -> form.disable())
//                  .httpBasic(basic -> basic.disable())
//                    .csrf(csrf -> csrf.disable());

                   return http.build();

    }         



    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean UserDetailsService userDetailsService(UserRepository userRepository){
        return new JpaUserDetailService(userRepository);
    }
}


