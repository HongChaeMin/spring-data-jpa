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
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    public void findTest() {
        Member member = new Member("tester1");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        System.out.println(Assertions.assertThat(findMember.getId()).isEqualTo(member.getId()));
        System.out.println(Assertions.assertThat(findMember.getUserName()).isEqualTo(member.getUserName()));
        System.out.println(Assertions.assertThat(findMember).isEqualTo(member));
    }

}
