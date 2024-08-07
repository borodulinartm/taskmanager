package org.example.task_manager.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.task_manager.models.User;
import org.example.task_manager.service.UserService;
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
    private final UserService userDetailsService;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserService service, PasswordEncoder encoder) {
        this.userDetailsService = service;
        this.encoder = encoder;
    }

    @GetMapping("/register")
    public ModelAndView registerForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("auth/registration");
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute(name = "user") User user, Errors errors,
                                     HttpServletRequest req) {
        if (errors.hasErrors()) {
            return new ModelAndView("auth/registration");
        }

        String password = user.getPassword();

        // Set user and sign in automatically
        userDetailsService.createUser(user, encoder);

        String username = user.getUsername();
        userDetailsService.authenticateUser(username, password, req);

        return new ModelAndView("redirect:/2fa");
    }
}
