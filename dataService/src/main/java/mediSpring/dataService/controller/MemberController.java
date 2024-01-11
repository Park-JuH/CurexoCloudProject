package mediSpring.dataService.controller;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public String registerMember(Member member) {
        memberService.join(member);
        return "redirect:/"; // Redirect after registration
    }

    @PostMapping("/findPassword")
    public String findPassword(String userName) {
        return "redirect:/"; // Redirect after registration
    }
}
