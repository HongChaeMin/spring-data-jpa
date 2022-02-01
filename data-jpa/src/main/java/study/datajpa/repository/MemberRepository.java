package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

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
    List<Member> findListByUserName(String userName); // 컬렉션
    Member findMemberByUserName(String userName); // 단건
    Optional<Member> findOptionalByUserName(String userName); //단건 Optional

    // 얘는 파라미터 값 이름이랑 엔티티 이름이랑 같아야하나봄

    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    List<Member> findListByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // 필수로 넣어주어야함
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age")int age);

    // join fetch 를 하면 연관관계에 있는 db를 다 조회함
    // 프록시 객체를 안만들고 team 객체를 다 가져와서 .getClass로 조회를 해보면 entity class가 채워져있음
    @Query("select m from Member m left join fetch m.team")
    List<Member> finMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) // 내부적으로 fetch 조인을 함
    List<Member> findAll();

    // @Query도 됨
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // named query도 됨
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUserName(@Param("userName") String username);

    // @NamedEntityGraph도 됨
    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUserName(@Param("userName") String username);

    // hibernate가 제공해줌
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(String username);

    // select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockByUserName(String username);

}
