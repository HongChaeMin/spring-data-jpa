package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Rollback(false)
public class MemberJpaRepositoryTests {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    public void findTest() {
        Member member = new Member("tester1");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        System.out.println(Assertions.assertThat(findMember.getId()).isEqualTo(member.getId()));
        System.out.println(Assertions.assertThat(findMember.getUserName()).isEqualTo(member.getUserName()));
        System.out.println(Assertions.assertThat(findMember).isEqualTo(member));
    }

    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

    }

    public void namedQuery() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        System.out.println(memberJpaRepository.findByUserName("member1"));
    }

    @Transactional
    public void paging() {
        List<Member> result = memberJpaRepository.findByPage(10, 0, 3);
        Long totalCount = memberJpaRepository.totalCont(10);

        for (Member m : result) {
            System.out.println(m);
        }
        System.out.println("totalCount : " + totalCount);

    }

    @Test
    public void bulkUpdate() {
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        System.out.println(resultCount);
    }

}
