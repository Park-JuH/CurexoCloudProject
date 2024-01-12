package mediSpring.dataService.service;

import mediSpring.dataService.domain.Member;
import mediSpring.dataService.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Member member = (Member) memberRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + name));

        return new org.springframework.security.core.userdetails.User(member.getName(),
                member.getPwd(),
                Collections.emptyList());
    }
}
