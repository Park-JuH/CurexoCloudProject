package mediSpring.dataService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        System.out.println("HomeController.home");
        return "login"; // Name of the view (HTML or template) to be displayed
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // Refers to signup.html in the templates directory
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @GetMapping("mainPage")
    public String mainPage() {
        return "mainPage";
    }
}
