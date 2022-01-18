package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest
@Rollback(false)
@Transactional
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    public void findTest() {
        Member member = new Member("tester1");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        System.out.println(Assertions.assertThat(findMember.getId()).isEqualTo(member.getId()));
        System.out.println(Assertions.assertThat(findMember.getUserName()).isEqualTo(member.getUserName()));
        System.out.println(Assertions.assertThat(findMember).isEqualTo(member));
    }

    public void findByUserNameAndAgeGreaterThan() {
        Member member1 = new Member("tester1", 10);
        Member member2 = new Member("tester2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        System.out.println(memberRepository.findByUserNameAndAgeGreaterThan("tester1", 10));

    }

    @Test
    public void finByAgeOrderByAgeDese() {
        Member member1 = new Member("tester1", 10);
        Member member2 = new Member("tester1", 20);

        System.out.println(memberRepository.findByUserNameOrderByAgeDesc("tester1"));
    }

}
