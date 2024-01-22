package mediSpring.dataService.service;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        // Check for duplicate member name
        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            // Handle the case where a member with the same name exists
            // This could be throwing an exception or returning a specific value
            return null;
//            throw new DataIntegrityViolationException("A member with the name " + member.getName() + " already exists.");
        }
        else {
            String encodedPassword = passwordEncoder.encode(member.getPassword());
            member.setPassword(encodedPassword);
            memberRepository.save(member);
            return member.getId();
        }
    }

    private void validateDuplicateMember(Member member) {
        // Add your logic to check for duplicate members
        // Throw an exception or handle as required
    }
}
