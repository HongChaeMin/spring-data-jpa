package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Rollback(false)
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;
    // em.flush() 실제 디비로 쿼리 날림 (영속성 컨텍스트 남아있음)
    // em.clear() 영속성 컨텍스트 캐쉬 삭제

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

    public void finByAgeOrderByAgeDese() {
        Member member1 = new Member("tester1", 10);
        Member member2 = new Member("tester1", 20);

        System.out.println(memberRepository.findByUserNameOrderByAgeDesc("tester1"));
    }

    public void testQuery() {
        Member member1 = new Member("tester1", 10);
        Member member2 = new Member("tester2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        System.out.println(memberRepository.findUser("tester1", 10));
    }

    public void testFindUserNameList() {
        System.out.println(memberRepository.findUserNameList());
    }

    public void testFinMemberDto() {
        System.out.println(memberRepository.findMemberDto());
    }

    public void testFindMembers() {
        List<String> names = new ArrayList<>();
        names.add("tester1");
        names.add("tester2");

        System.out.println(memberRepository.findMembers(names));
    }

    @Test
    public void testReturnType() {
        // collection 으로 받을 때 데이터가 없으면 null이 아니라 empty collection으로 반환 시켜줌
        // 단건 조회로 받을 때 데이터가 없으면 null 반환
        // optional 쓰는게 좋음...!

        System.out.println(memberRepository.findListByUserName("tester1"));
        System.out.println(memberRepository.findMemberByUserName("tester1"));
        System.out.println(memberRepository.findOptionalByUserName("tester1"));
    }

    @Transactional
    public void paging() {

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        // Page
        Page<Member> page = memberRepository.findByAge(10, pageable);

        List<Member> members = page.getContent();

        for (Member m : members) {
            System.out.println(m);
        }

        // 추가 count 쿼리 결과를 포함하는 페이징
        System.out.println("size : " + members.size());
        System.out.println("total content count : " + page.getTotalElements());
        System.out.println("page number : " + page.getNumber());
        System.out.println("total page count : " + page.getTotalPages());
        System.out.println("is first : " + page.isFirst());
        System.out.println("is last : " + page.isLast());

        // Slice
        Slice<Member> memberSlice = memberRepository.findSliceByAge(10, pageable);

        // 추가 count 쿼리 없이 다음 페이지만 확인 가능 (내부적으로 limit + 1조회)
        System.out.println("size : " + members.size());
        System.out.println("page number : " + memberSlice.getNumber());
        System.out.println("is first : " + memberSlice.isFirst());
        System.out.println("is last : " + memberSlice.isLast());

        // List
        // 추가 count 쿼리 없이 결과만 반환

        // entity를 dto로 쉽게 변환하는 방법
        page.map(member -> {
            return new MemberDTO(member.getId(), member.getUserName(), member.getTeam().getName());
        });

    }

    @Transactional
    public void bulkUpdate() {
        int resultCount = memberRepository.bulkAgePlus(20);

        // 기본적으로 save를 하면 data jpa가 db에 반영을 하고 다음동작을 실행함

        // Member member = memberRepository.findMemberByUserName("tester1");

        // 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와
        // DB에 엔티티 상태가 달라질 수 있다.

        // System.out.println("member의 결과가 반영이 안되어 있다 : " + member);

        // 지금은 @Modifying에 속성을 넣어줘야 바뀌는듯 이렇게 해도 안됨
        // em.clear();

        Member member2 = memberRepository.findMemberByUserName("tester1");

        System.out.println("member의 결과가 반영이 되었다 : " + member2);
    }

    @Transactional
    public void findMemberLazy() {
        // tester1 -> TeamA
        // tester2 -> TeamB

        List<Member> result = memberRepository.findAll();

        // select 쿼리를 한 번 날림
        for (Member m : result) {
            System.out.println("member : " + m);
        }

        // N + 1
        for (Member m : result) {
            System.out.println("member : " + m);

            // member만 가져와서 null로 보낼 수 없으니 가짜 엔티티를 만듦 (entity.Team$HibernateProxy$gCaEbUQv)
            System.out.println("member.team.class : " + m.getTeam().getClass());

            // 이때 team을 조회함
            Hibernate:
            /* select
            team0_.team_id as team_id1_1_0_,
                    team0_.name as name2_1_0_
            from
            team team0_
            where
            team0_.team_id=? */
            System.out.println("member.team : " + m.getTeam().getName());
        }

    }

    @Transactional
    public void queryHint() {
        // Member findMember = memberRepository.findById(4L).get();

        // 변경 감지(더티체킹)해서 디비에 쿼리를 날려버림
        // findMember.setUserName("member2");
        // 변경 감지를 위해서는 어쨋든 비교 대상이 있어야하기 때문에 2개의 데이터를 가지고 있어서 성능이 감소됨

        Member findMember2 = memberRepository.findReadOnlyByUserName("member2");

        // update query 실행 x
        findMember2.setUserName("tester2");
    }

    @Transactional
    public void lock() {
        Member findMember = memberRepository.findLockByUserName("tester1");
    }

    @Test
    public void custom() {
        /* Hibernate:
        select
        member0_.member_id as member_i1_0_,
                member0_.age as age2_0_,
        member0_.team_id as team_id4_0_,
                member0_.user_name as user_nam3_0_
        from
        member member0_ */
        memberRepository.findMemberCustom();
    }

}
