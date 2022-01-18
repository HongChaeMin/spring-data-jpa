package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

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

}
