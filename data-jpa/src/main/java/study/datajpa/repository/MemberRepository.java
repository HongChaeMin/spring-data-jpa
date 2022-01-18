package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 문제점 : 이름이 계속 길어진다
    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    List<Member> findByUserNameOrderByAgeDesc(String username);

    // 실무에서 많이 안쓰인다
    @Query(name = "Member.findByUserName") // named query로 실행할 때는 @Param 붙어줘야됨
    List<Member> findByUserName(@Param("username") String username);

    // 실무에서 많이 쓰인다
    // 이름이 없는 named query라고 생각하면 된다 (실행 전에 에러를 찾을 수 있음)
    // 동적쿼리는 Querydsl 쓰세요...^^
    @Query("select m from Member m where m.userName = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    // DTO로 변환 가능 new DTO()
    @Query("select new study.datajpa.dto.MemberDTO(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDto();

    // 파라미터 바인딩
    // - 위치 기반(안씀), - 이름 기반
    @Query("select m from Member m where m.userName in :names")
    List<Member> findMembers(@Param("names") List<String> names);

    // spring data jpa는 유연한 반환타입 지원
    // 레퍼런스에 리턴 타입 정의되어 있음
    List<Member> findListByUsername(String name); // 컬렉션
    Member findMemberByUsername(String name); // 단건
    Optional<Member> findOptionalByUsername(String name); //단건 Optional

}
