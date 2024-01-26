package mediSpring.dataService.controller;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.repository.MemberRepository;
import mediSpring.dataService.service.MemberService;
import mediSpring.dataService.security.SecurityConfig;
//import mediSpring.dataService.service.MemberUserDetailsService;
import mediSpring.dataService.service.MemberUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberUserDetailsService memberUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;


    //    RedirectAttributes redirectAttributes;
    @Autowired
    public MemberController(MemberService memberService, MemberUserDetailsService memberUserDetailsService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberUserDetailsService = memberUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
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
    public String findPassword(String username, RedirectAttributes redirectAttributes) {
        System.out.println("username = " + username);

        Optional<Member> forgotMember = memberRepository.findByUsername(username);

        if (forgotMember.isPresent()) {
            // Initiate a password reset process instead of returning the password
            // For example, send an email with a password reset link

            redirectAttributes.addFlashAttribute("message", "Check E-mail.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid Username.");
        }
        return "redirect:/forgotPassword";
    }

    @PostMapping("/login")
    public String userLogin(Member member, RedirectAttributes redirectAttributes) {
        System.out.println("MemberController.userLogin");
        System.out.println("member = " + member.getUsername());
        UserDetails userDetails = memberUserDetailsService.loadUserByUsername(member.getUsername());

        if (passwordEncoder.matches(member.getPassword(), userDetails.getPassword())) {
            // Passwords match
            System.out.println("Password matches!");
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return "redirect:/mainPage";
            } catch (AuthenticationException e) {
                // 인증 예외 처리 (예: 잘못된 자격 증명)
                System.out.println("e = " + e);
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Invalid information");
                return "redirect:/"; // 오류와 함께 로그인 페이지로 리디렉션
            }
        } else {
            // Passwords do not match
            System.out.println("Invalid Password");
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Invalid information");
            // Here you can define what to do in case of failed login
            return "redirect:/";
        }
    }
}
