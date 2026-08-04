package org.app.quiz_application.controller;

import jakarta.validation.Valid;
import org.app.quiz_application.model.User;
import org.app.quiz_application.service.QuizUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final QuizUserDetailsService userService;

    public UserController(QuizUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("loginError", "Invalid username or password");
        return "login";
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result) {
        if (result.hasErrors()) return "registration";
        String response = userService.registerUser(user.getUsername(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
        if (response.equals("Username already exists")) {
            result.rejectValue("username", "duplicate", response);
            return "registration";
        }
        return "redirect:/user/login?registered";
    }
}
