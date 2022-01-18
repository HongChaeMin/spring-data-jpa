package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    List<Member> findByUserNameOrderByAgeDesc(String username);

    @Query(name = "Member.findByUserName") // named query로 실행할 때는 @Param 붙어줘야됨
    List<Member> findByUserName(@Param("username") String username);

}
