package study.datajpa.repository;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {

    // LockModeType.PESSIMISTIC_FORCE_INCREMENT : 횟수가 늘어나면 늘어날수록 정확성이 떨어짐 (@Version 과 함께 사용)
    // 이게 시간때문에 안되는건지... 그냥 안되는건지 모르겠다...
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    List<Notice> findAllByIdIn(@Param("Id") List<Long> Id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Notice findByMemberIdAndTeamIdAndTitle(Long memberId, Long teamId, String title);

}
