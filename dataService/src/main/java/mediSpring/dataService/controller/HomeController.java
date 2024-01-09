package mediSpring.dataService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "mainpage"; // Name of the view (HTML or template) to be displayed
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // Refers to signup.html in the templates directory
    }
}
