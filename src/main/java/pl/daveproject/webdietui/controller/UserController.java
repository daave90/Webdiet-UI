package pl.daveproject.webdietui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.service.AuthService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/profile")
    public String getAllProducts() {
        try {
            authService.checkIfAuthenticated();
        } catch (WebdietRestException exception) {
            return "redirect:/login";
        }
        return "user";
    }
}
