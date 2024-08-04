package org.example.task_manager.controller;

import jakarta.validation.Valid;
import org.example.task_manager.models.User;
import org.example.task_manager.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(CustomUserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @GetMapping("/register")
    public ModelAndView registerForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("auth/registration");
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute(name = "user") User user, Errors errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("auth/registration");
        }

        userDetailsService.createUser(user, encoder);
        return new ModelAndView("redirect:/login");
    }
}
