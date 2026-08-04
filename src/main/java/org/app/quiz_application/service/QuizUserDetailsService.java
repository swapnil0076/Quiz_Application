package org.app.quiz_application.service;

import org.app.quiz_application.model.User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public QuizUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String role = user.getRole() == null || user.getRole().isBlank()
                ? "USER" : user.getRole().toUpperCase();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(role)
                .build();
    }

    public User loadUserByUsername(String username,String password){

        User user = users.get(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;

    }

    public String registerUser(String username, String password, String firstName,
                               String lastName, String email, String role) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return "Username and password are required";
        }
        if (users.containsKey(username)) {
            return "Username already exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRole(role);

        users.put(username,user);
        return "User Registered Successfully";

    }

}
