package com.kinoxp.security;

import com.kinoxp.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http                .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/", "/index.html").permitAll()
                         .requestMatchers("/html/**", "/css/**", "/js/**").permitAll()
                         .requestMatchers(HttpMethod.POST, "/kino/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/kino/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/kino/movies/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/kino/movies/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/kino/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/kino/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/kino/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/kino/showings/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/kino/showings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/kino/showings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/kino/showings/**").hasRole("ADMIN")

                           .anyRequest().authenticated()
                )

                .formLogin( form -> form
                        .loginPage("/html/login.html")
                        .loginProcessingUrl("/kino/users/login")
                        .defaultSuccessUrl("/html/index.html", true)
                        .permitAll()

                )

                .logout(logout -> logout
                  .logoutUrl("/kino/users/logout")
                        .logoutSuccessUrl("/html/login.html")
                        .permitAll()


                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if ("/kino/movies/create".equals(request.getRequestURI())) {
                                response.setStatus(401);
                                response.setContentType(MediaType.TEXT_HTML_VALUE);
                                response.setCharacterEncoding("UTF-8");
                                response.getWriter().write("""
                                        <html><body><script>
                                        alert('Du har ikke adgang.');
                                        window.location.href='/html/index.html';
                                        </script></body></html>
                                        """);
                                return;
                            }

                            response.sendRedirect("/html/login.html");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if ("/kino/movies/create".equals(request.getRequestURI())) {
                                response.setStatus(403);
                                response.setContentType(MediaType.TEXT_HTML_VALUE);
                                response.setCharacterEncoding("UTF-8");
                                response.getWriter().write("""
                                        <html><body><script>
                                        alert('Du har ikke adgang, da du ikke er admin.');
                                        window.location.href='/html/index.html';
                                        </script></body></html>
                                        """);
                                return;
                            }

                            response.setStatus(403);
                            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            if (accessDeniedException instanceof InvalidCsrfTokenException
                                    || accessDeniedException instanceof MissingCsrfTokenException) {
                                response.getWriter().write("Sikkerhedsfejl (CSRF). Opdater siden og prøv igen.");
                            } else {
                                response.getWriter().write("Du har ikke adgang, da du ikke er admin.");
                            }
                        })
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/kino/users/login", "/kino/users")
                );



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


