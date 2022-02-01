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

import java.util.ArrayList;
import java.util.List;

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

    @Test
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


}
