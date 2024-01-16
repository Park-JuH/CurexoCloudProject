package mediSpring.dataService.controller;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.service.MemberService;
//import mediSpring.dataService.service.MemberUserDetailsService;
import mediSpring.dataService.service.MemberUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberUserDetailsService memberUserDetailsService;
    private final PasswordEncoder passwordEncoder;

//    RedirectAttributes redirectAttributes;
    @Autowired
    public MemberController(MemberService memberService, MemberUserDetailsService memberUserDetailsService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.memberUserDetailsService = memberUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/join")
    public String registerMember(Member member, RedirectAttributes redirectAttributes) {
        Long memberId = memberService.join(member);
        if (memberId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Member name exists.");
            return "redirect:/signup";
        }
        return "redirect:/"; // Redirect after registration
    }

    @PostMapping("/findPassword")
    public String findPassword(String userName) {
        return "redirect:/"; // Redirect after registration
    }

    @PostMapping("/login")
    public String userLogin(Member member) {
        System.out.println("MemberController.userLogin");
        System.out.println("member = " + member.getName());
        UserDetails userDetails = memberUserDetailsService.loadUserByUsername(member.getName());

        if (passwordEncoder.matches(member.getPwd(), userDetails.getPassword())) {
            // Passwords match
            System.out.println("Password matches!");
            // Here you can define what to do next after successful login
            return "redirect:/mainPage";
        } else {
            // Passwords do not match
            System.out.println("Password does not match.");
            // Here you can define what to do in case of failed login
            return "redirect:/";
        }
    }
}
