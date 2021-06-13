package pl.daveproject.webdietui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.request.LoginRequest;
import pl.daveproject.webdietui.model.request.UserRequest;
import pl.daveproject.webdietui.service.AuthService;
import pl.daveproject.webdietui.validators.FormValidator;
import pl.daveproject.webdietui.validators.UserRequestValidator;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(UserRequest userRequest, Model model) {
        try {
            FormValidator validator = new UserRequestValidator();
            validator.validate(userRequest);
            authService.register(userRequest);
        } catch (WebdietRestException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("userRequser", userRequest);
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, Model model) {
        try {
            authService.login(loginRequest);
        } catch (WebdietRestException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("loginRequest", loginRequest);
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        try {
            authService.checkIfAuthenticated();
            authService.logout();
            return "redirect:/login";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }
}
