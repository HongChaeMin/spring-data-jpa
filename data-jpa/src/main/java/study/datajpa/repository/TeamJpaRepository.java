package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Team save(Team team) {
        entityManager.persist(team);
        return team;
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

    public List<Team> findAll() {
        return entityManager.createQuery("select t from Team t", Team.class).getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = entityManager.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public long count() {
        return entityManager.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }

    public void delete(Team team) {
        entityManager.remove(team);
    }

}
