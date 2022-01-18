package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Rollback(false)
public class MemberTests {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void test() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("tester1", 20, teamA);
        Member member2 = new Member("tester2", 21, teamA);
        Member member3 = new Member("tester3", 22, teamB);
        Member member4 = new Member("tester4", 23, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        // 강제로 인서트 쿼리 날림
        entityManager.flush();
        // 영속성 컨텍스트에 있는 캐시 날림
        entityManager.clear();

        // 확인
        List<Member> members = entityManager.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

}
