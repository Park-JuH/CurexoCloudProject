package mediSpring.dataService.service;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Long join(Member member) {
        // You can add additional logic here (e.g., checking for duplicate members)
        String encodedPassword = passwordEncoder.encode(member.getPwd());
        member.setPwd(encodedPassword);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Add your logic to check for duplicate members
        // Throw an exception or handle as required
    }
}
