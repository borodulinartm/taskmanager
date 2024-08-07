package org.example.task_manager.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.exceptions.ConfirmationCodeNotMatchedException;
import org.example.task_manager.models.User;
import org.example.task_manager.security.TwoFactorUsernamePasswordToken;
import org.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@Slf4j
public class ConfirmCodeController {
    private final UserService userService;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    @Autowired
    public ConfirmCodeController(UserService userService,
                                 AuthenticationSuccessHandler successHandler,
                                 AuthenticationFailureHandler failureHandler) {
        this.userService = userService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @GetMapping("/2fa")
    public ModelAndView show2fa(Model model) {
        userService.generateConfirmationCode(
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        model.addAttribute("user", new User());
        return new ModelAndView("auth/confirmCode");
    }

    @PostMapping("/2fa")
    public void checkConfirmCode(@ModelAttribute(name = "user") User code,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        String confirmCode = code.getCode();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        User currentUser = (User) auth.getPrincipal();
        if (userService.verifyConfirmationCode(currentUser, confirmCode)) {
            userService.acceptUser(currentUser);

            // Check for validity code
            Authentication fullAuth = new UsernamePasswordAuthenticationToken(
                    auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(fullAuth);

            successHandler.onAuthenticationSuccess(request, response, auth);
        } else {
            failureHandler.onAuthenticationFailure(request, response,
                    new ConfirmationCodeNotMatchedException("Confirmation code not matched"));
        }
    }
}
