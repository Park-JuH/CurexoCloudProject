//package mediSpring.dataService;
//
//import mediSpring.dataService.repository.MemberRepository;
//import mediSpring.dataService.service.MemberService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MediConfig {
//    private final MemberRepository memberRepository;
//
//    public SpringConfig(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//
//    @Bean
//    public MemberService memberService() {
//        return new MemberService(memberRepository);
//    }
//
////    @Bean
////    public MemberRepository memberRepository() {
//////        return new MemoryMemberRepository();
//////        return new JdbcMemberRepository(dataSource);
//////        return new JdbcTemplateMemberRepository(dataSource);
//////        return new JpaMemberRepository(em);
////    }
//}