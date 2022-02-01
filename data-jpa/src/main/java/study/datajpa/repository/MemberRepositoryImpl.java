package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    // 화면에 필요한 DTO를 가져오는 복잡한 로직과
    // 비지니스 로직의 레파지토리는 분리하는것이 좋음

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
