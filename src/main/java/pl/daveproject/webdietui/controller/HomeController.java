package pl.daveproject.webdietui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.service.AuthService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AuthService authService;

    @GetMapping("/")
    public String getHomePage() {
        try {
            authService.checkIfAuthenticated();
        } catch (WebdietRestException exception) {
            return "redirect:/login";
        }
        return "redirect:/show-recipes";
    }
}
