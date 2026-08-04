package org.app.quiz_application.config;

import org.app.quiz_application.service.QuizUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                             QuizUserDetailsService userDetailsService)
            throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user", "/user/login", "/user/register",
                                "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/quiz/quizList", "/quiz/addQuiz",
                                "/quiz/editQuiz/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/quiz/questions")
                                .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/quiz/questions/**")
                                .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/quiz/questions/**")
                                .hasRole("ADMIN")
                        .requestMatchers("/quiz/quiz/**", "/quiz/submit", "/quiz/result",
                                "/quiz/submit-form", "/quiz/result-page")
                                .hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .successHandler(roleBasedSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/user/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            response.sendRedirect(request.getContextPath()
                    + (isAdmin ? "/quiz/quizList" : "/quiz/quiz"));
        };
    }
}
