package mediSpring.dataService.service;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member) {
        // You can add additional logic here (e.g., checking for duplicate members)
        memberRepository.save(member);
        return member.getId();
    }
}
